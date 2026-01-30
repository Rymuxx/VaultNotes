package com.example.vaultnotes.ui.camera

import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PantallaCamara(alCapturarFoto: (Uri) -> Unit) {
    val contexto = LocalContext.current
    val cicloVidaOwner = LocalLifecycleOwner.current
    val capturadorImagen = remember { ImageCapture.Builder().build() }
    val proveedorCamaraFuture = remember { ProcessCameraProvider.getInstance(contexto) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)
                proveedorCamaraFuture.addListener({
                    val cameraProvider = proveedorCamaraFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            cicloVidaOwner,
                            cameraSelector,
                            preview,
                            capturadorImagen
                        )
                    } catch (ex: Exception) {
                        Log.e("Cámara", "Error al vincular cámara", ex)
                    }
                }, executor)
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Button(
            onClick = {
                val nombreArchivo = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                    .format(System.currentTimeMillis()) + ".jpg"
                val archivo = File(contexto.filesDir, nombreArchivo)
                val opciones = ImageCapture.OutputFileOptions.Builder(archivo).build()

                capturadorImagen.takePicture(
                    opciones,
                    ContextCompat.getMainExecutor(contexto),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            alCapturarFoto(Uri.fromFile(archivo))
                        }
                        override fun onError(exception: ImageCaptureException) {
                            Log.e("Cámara", "Error al capturar", exception)
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
        ) {
            Text("Capturar Foto")
        }
    }
}
