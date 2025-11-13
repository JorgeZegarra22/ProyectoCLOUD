package com.example.proyecto

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScannerActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var pickImageButton: Button
    private lateinit var scanButton: Button
    private lateinit var resultText: TextView
    private lateinit var scanStatusText: TextView
    private lateinit var statusText: TextView
    private lateinit var scanOverlay: RelativeLayout
    private lateinit var scanLine: View
    private lateinit var statusIndicator: View
    private lateinit var scanProgress: ProgressBar
    private lateinit var cornerTopLeft: ImageView
    private lateinit var cornerTopRight: ImageView
    private lateinit var cornerBottomLeft: ImageView
    private lateinit var cornerBottomRight: ImageView

    private lateinit var scanLineAnimation: Animation
    private lateinit var cornersAnimation: Animation
    private lateinit var pulseAnimation: Animation
    private var isScanning = false

    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imageView.setImageURI(it)
            scanButton.isEnabled = true
            updateStatus("Imagen cargada - Listo para escanear", "#4CAF50")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupAnimations()
        setupClickListeners()
    }

    private fun initViews() {
        imageView = findViewById(R.id.imageView)
        pickImageButton = findViewById(R.id.pickImageButton)
        scanButton = findViewById(R.id.scanButton)
        resultText = findViewById(R.id.resultText)
        scanStatusText = findViewById(R.id.scanStatusText)
        statusText = findViewById(R.id.statusText)
        scanOverlay = findViewById(R.id.scanOverlay)
        scanLine = findViewById(R.id.scanLine)
        statusIndicator = findViewById(R.id.statusIndicator)
        scanProgress = findViewById(R.id.scanProgress)

        cornerTopLeft = findViewById(R.id.cornerTopLeft)
        cornerTopRight = findViewById(R.id.cornerTopRight)
        cornerBottomLeft = findViewById(R.id.cornerBottomLeft)
        cornerBottomRight = findViewById(R.id.cornerBottomRight)
    }

    private fun setupAnimations() {
        scanLineAnimation = AnimationUtils.loadAnimation(this, R.anim.scan_line_animation)
        cornersAnimation = AnimationUtils.loadAnimation(this, R.anim.corners_fade_in)
        pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation)
    }

    private fun setupClickListeners() {
        pickImageButton.setOnClickListener { selectImage() }
        scanButton.setOnClickListener { startScanAnimation() }
    }

    private fun selectImage() {
        imagePickerLauncher.launch("image/*")
    }

    private fun startScanAnimation() {
        if (isScanning || selectedImageUri == null) return

        isScanning = true

        scanOverlay.visibility = View.VISIBLE
        showScanCorners()
        scanLine.startAnimation(scanLineAnimation)
        statusIndicator.startAnimation(pulseAnimation)

        updateStatus("Escaneando imagen...", "#FF9800")
        updateStatusIndicator(R.drawable.status_indicator_scanning)

        lifecycleScope.launch {
            delay(2000)
            sendImageToApi()
        }
    }



    private fun sendImageToApi() {
        val file = FileUtil.from(this, selectedImageUri!!)
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        RetrofitClient.instance.uploadImage(body).enqueue(object : Callback<PredictResponse> {
            override fun onResponse(call: Call<PredictResponse>, response: Response<PredictResponse>) {
                val prediction = response.body()?.prediction ?: "Sin resultado"
                completeScan(prediction)
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                completeScan("Error al escanear: ${t.message}")
            }
        })
    }

    private fun completeScan(prediction: String) {
        isScanning = false

        scanLine.clearAnimation()
        statusIndicator.clearAnimation()
        scanOverlay.visibility = View.GONE
        hideScanCorners()

        val traduccion = traducirPrediccion(prediction)
        val descripcion = obtenerDescripcion(prediction)

        resultText.text = "\u2705 Escaneo completado!\n\nResultado: $traduccion"

        // Asegúrate de tener un TextView adicional para esto
        val descripcionText = findViewById<TextView>(R.id.descripcionText)
        descripcionText.text = descripcion

        updateStatus("Escaneo completado", "#4CAF50")
        updateStatusIndicator(R.drawable.status_indicator_ready)
        animateSuccess()
    }

    fun traducirPrediccion(prediccion: String): String {
        return when (prediccion.lowercase()) {
            "tomato___bacterial_spot" -> "Mancha bacteriana del tomate"
            "tomato___early_blight" -> "Tizón temprano del tomate"
            "tomato___late_blight" -> "Tizón tardío del tomate"
            "tomato___leaf_mold" -> "Moho foliar del tomate"
            "tomato___septoria_leaf_spot" -> "Mancha foliar por Septoria"
            "tomato___spider_mites two-spotted_spider_mite" -> "Ácaros de dos manchas del tomate"
            "tomato___target_spot" -> "Mancha objetivo del tomate"
            "tomato___tomato_yellow_leaf_curl_virus" -> "Virus del rizado amarillo del tomate"
            "tomato___tomato_mosaic_virus" -> "Virus del mosaico del tomate"
            "tomato___healthy" -> "Sano"
            else -> prediccion
        }
    }

    fun obtenerDescripcion(prediccion: String): String {
        return when (prediccion.lowercase()) {
            "tomato___bacterial_spot" -> "La mancha bacteriana del tomate es causada por bacterias que generan manchas oscuras en hojas, tallos y frutos. Puede reducir significativamente el rendimiento si no se controla."
            "tomato___early_blight" -> "El tizón temprano del tomate es una enfermedad fúngica que provoca manchas circulares con anillos concéntricos en hojas, debilitando la planta y reduciendo la producción."
            "tomato___late_blight" -> "El tizón tardío es una enfermedad devastadora causada por un hongo, que ocasiona manchas marrones grandes en hojas y frutos, y puede matar rápidamente la planta."
            "tomato___leaf_mold" -> "El moho foliar es causado por hongos y se manifiesta como manchas amarillas en la parte superior de la hoja y moho gris en la inferior. Común en ambientes húmedos."
            "tomato___septoria_leaf_spot" -> "La mancha foliar por Septoria es una enfermedad fúngica que produce pequeñas manchas circulares marrones con centros grises, acelerando la caída de hojas."
            "tomato___spider_mites two-spotted_spider_mite" -> "Los ácaros de dos manchas son plagas microscópicas que chupan la savia de las hojas, causando puntos amarillos y debilitando la planta."
            "tomato___target_spot" -> "La mancha objetivo del tomate provoca lesiones circulares con centros claros y bordes oscuros. Puede afectar hojas, tallos y frutos."
            "tomato___tomato_yellow_leaf_curl_virus" -> "El virus del rizado amarillo es transmitido por mosca blanca. Provoca enrollamiento y amarillamiento de hojas, retrasando el crecimiento de la planta."
            "tomato___tomato_mosaic_virus" -> "El virus del mosaico del tomate causa manchas irregulares verdes y amarillas en las hojas, deformaciones y reducción del vigor de la planta."
            "tomato___healthy" -> "La hoja de tomate se encuentra sana, sin señales visibles de enfermedades ni daños por plagas."
            else -> "No se tiene información disponible sobre esta condición."
        }
    }



    private fun showScanCorners() {
        listOf(cornerTopLeft, cornerTopRight, cornerBottomLeft, cornerBottomRight).forEach { corner ->
            corner.visibility = View.VISIBLE
            corner.startAnimation(cornersAnimation)
        }
    }

    private fun hideScanCorners() {
        listOf(cornerTopLeft, cornerTopRight, cornerBottomLeft, cornerBottomRight).forEach { corner ->
            corner.visibility = View.GONE
        }
    }

    private fun updateStatus(message: String, color: String) {
        statusText.text = message
        statusText.setTextColor(Color.parseColor(color))
    }

    private fun updateStatusIndicator(drawableRes: Int) {
        statusIndicator.setBackgroundResource(drawableRes)
    }

    private fun animateSuccess() {
        val bounceIn = AnimationUtils.loadAnimation(this, R.anim.bounce_in).apply {
            duration = 600
        }
        resultText.startAnimation(bounceIn)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    private fun updateScanStatus(status: String) {
        scanStatusText.text = status
    }

    private fun saveScanToHistory(imageUri: Uri, pred: String, desc: String) {
        val dao = ScanDatabase.getDatabase(this).scanHistoryDao()

        lifecycleScope.launch(Dispatchers.IO) {
            dao.insertScan(
                ScanHistory(
                    imageUri = imageUri.toString(),
                    prediction = pred,
                    description = desc,
                    date = System.currentTimeMillis()
                )
            )
        }
    }
 }

