# Detector de enfermedades de las hojas de tomate

Aplicacion Android para detectar enfermedades en hojas de tomate a partir de una imagen. El proyecto combina una interfaz movil en Kotlin con una API de prediccion desplegada en Hugging Face, permitiendo que el usuario seleccione una foto, la envie al modelo y reciba un diagnostico entendible en espanol.

Este repositorio corresponde a un proyecto academico orientado al uso de computacion en la nube, consumo de servicios externos e inteligencia artificial aplicada al cuidado de cultivos.

## Descripcion

La aplicacion ayuda a identificar visualmente posibles enfermedades en hojas de tomate. Desde el celular, el usuario puede cargar una imagen de la hoja, iniciar el escaneo y obtener una respuesta con:

- Nombre de la enfermedad o estado detectado.
- Traduccion del resultado tecnico generado por el modelo.
- Descripcion breve de la condicion encontrada.
- Interfaz animada que simula el proceso de escaneo.
- Historial local de escaneos realizados.

El analisis no se ejecuta dentro del telefono. La imagen se envia a una API externa, que procesa la prediccion y devuelve el resultado a la aplicacion.

## Funcionalidades principales

- Seleccion de imagenes desde la galeria.
- Envio de imagenes mediante una peticion multipart.
- Consumo de API REST con Retrofit.
- Interpretacion de resultados del modelo en espanol.
- Descripciones educativas sobre enfermedades del tomate.
- Registro local de historial con Room.
- Pantalla de historial con RecyclerView.
- Animaciones de escaneo y retroalimentacion visual para el usuario.

## Enfermedades y estados reconocidos

La aplicacion contempla las siguientes clases de prediccion:

- Mancha bacteriana del tomate.
- Tizon temprano.
- Tizon tardio.
- Moho foliar.
- Mancha foliar por Septoria.
- Acaros de dos manchas.
- Mancha objetivo.
- Virus del rizado amarillo del tomate.
- Virus del mosaico del tomate.
- Hoja sana.

## Tecnologias utilizadas

- Kotlin
- Android SDK
- Gradle Kotlin DSL
- Android Studio
- Retrofit
- OkHttp
- Gson Converter
- Room Database
- Coroutines
- RecyclerView
- Material Design
- AppCompat
- Jetpack Compose

## Arquitectura general

```text
Usuario
  |
  | selecciona imagen
  v
Aplicacion Android
  |
  | envia archivo por multipart/form-data
  v
API de prediccion en Hugging Face
  |
  | devuelve clase detectada
  v
App muestra diagnostico, descripcion e historial
```

## API de prediccion

La app consume el siguiente servicio:

```text
https://cocoliso22-tomato-leaf-disease-detector.hf.space/predict
```

El endpoint recibe una imagen en el campo multipart:

```text
file
```

La respuesta esperada contiene una prediccion, que luego se traduce dentro de la app a un nombre legible en espanol.

## Requisitos

- Android Studio.
- JDK 11 o superior.
- Android SDK 35.
- Dispositivo fisico o emulador con Android 8.0 o superior.
- Conexion a internet para consumir la API.

Configuracion principal del proyecto:

```text
compileSdk: 35
minSdk: 26
targetSdk: 35
applicationId: com.example.proyecto
```

## Instalacion

Clona el repositorio:

```bash
git clone https://github.com/JorgeZegarra22/ProyectoCLOUD.git
```

Entra al proyecto:

```bash
cd ProyectoCLOUD
```

Abre la carpeta en Android Studio y espera la sincronizacion de Gradle.

## Ejecucion

Desde Android Studio:

1. Selecciona un emulador o dispositivo fisico.
2. Sincroniza el proyecto con Gradle.
3. Ejecuta la app con el boton Run.

Desde terminal:

```bash
./gradlew assembleDebug
```

En Windows:

```bash
gradlew.bat assembleDebug
```

## Uso de la aplicacion

1. Abre la app.
2. Selecciona una imagen de una hoja de tomate.
3. Inicia el escaneo.
4. Espera la respuesta del modelo.
5. Revisa el diagnostico y la descripcion mostrada.
6. Consulta el historial para ver escaneos anteriores.

## Estructura principal

```text
app/src/main/java/com/example/proyecto/
|-- ApiService.kt
|-- RetrofitClient.kt
|-- ScannerActivity.kt
|-- HistoryActivity.kt
|-- ScanDatabase.kt
|-- ScanHistory.kt
|-- ScanHistoryDao.kt
|-- ScanHistoryAdapter.kt
|-- FileUtil.kt
`-- PredictResponse.kt
```

## Objetivo del proyecto

El objetivo es demostrar una solucion movil conectada a la nube que use inteligencia artificial para apoyar la deteccion temprana de enfermedades en cultivos de tomate. La app funciona como cliente Android, mientras que el procesamiento de imagenes se delega a un servicio externo desplegado en la nube.

## Autor

Desarrollado por Jorge Zegarra.
