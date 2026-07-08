# ProyectoCLOUD

ProyectoCLOUD es una aplicacion Android desarrollada en Kotlin para analizar imagenes de hojas de tomate y detectar posibles enfermedades mediante una API externa de prediccion.

La app permite seleccionar una imagen desde el dispositivo, enviarla al servicio de analisis y mostrar el resultado en espanol junto con una descripcion de la condicion detectada.

## Caracteristicas

- Seleccion de imagenes desde la galeria del dispositivo.
- Escaneo visual con animaciones durante el analisis.
- Envio de imagenes a una API mediante Retrofit y peticiones multipart.
- Traduccion de predicciones tecnicas a nombres comprensibles en espanol.
- Descripcion informativa para cada enfermedad detectada.
- Historial local de escaneos usando Room.
- Interfaz Android con Material Design, AppCompat y componentes Jetpack.

## Enfermedades detectadas

La aplicacion reconoce condiciones comunes en hojas de tomate, entre ellas:

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
- Retrofit
- Gson Converter
- OkHttp
- Room Database
- Coroutines
- Material Design
- Jetpack Compose
- AppCompat

## Requisitos

- Android Studio
- JDK 11 o superior
- Android SDK con `compileSdk 35`
- Conexion a internet para consumir la API de prediccion
- Dispositivo o emulador con Android 8.0 o superior (`minSdk 26`)

## Instalacion y ejecucion

1. Clona el repositorio:

```bash
git clone https://github.com/JorgeZegarra22/ProyectoCLOUD.git
```

2. Abre el proyecto en Android Studio.

3. Espera a que Gradle sincronice las dependencias.

4. Ejecuta la aplicacion en un emulador o dispositivo fisico.

Tambien puedes compilar desde consola:

```bash
./gradlew assembleDebug
```

En Windows:

```bash
gradlew.bat assembleDebug
```

## Uso

1. Abre la aplicacion.
2. Selecciona una imagen de una hoja de tomate.
3. Presiona el boton de escaneo.
4. Espera el resultado de la prediccion.
5. Revisa el diagnostico y la descripcion mostrada por la app.

## API

La aplicacion envia las imagenes al endpoint:

```text
https://cocoliso22-tomato-leaf-disease-detector.hf.space/predict
```

El envio se realiza como formulario multipart con el campo `file`.

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
`-- ScanHistoryAdapter.kt
```

## Autor

Proyecto desarrollado por Jorge Zegarra.
