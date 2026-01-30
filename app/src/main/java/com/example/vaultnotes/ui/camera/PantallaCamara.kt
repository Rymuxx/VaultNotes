package com.example.vaultnotes.ui.camera

import android.content.Context
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
import java.util.concurrent.Executor

@Composable
fun PantallaCamara(alCapturarFoto: (Uri) -> Unit) {
    val contexto = LocalContext.current
    val cicloVidaOwner = LocalLifecycleOwner.current
    val ejecutor = remember { ContextCompat.getMainExecutor(contexto) }
    
    val vistaPrevia = remember { PreviewView(contexto) }
    val capturaImagen = remember { ImageCapture.Builder().build() }
    val selectorCamara = CameraSelector.DEFAULT_BACK_CAMERA

    LaunchedEffect(selectorCamara) {
        val proveedorCamaraFuture = ProcessCameraProvider.getInstance(contexto)
        proveedorCamaraFuture.addListener({
            val proveedorCamara = proveedorCamaraFuture.get()
            val vistaPreviaConfigurada = Preview.Builder().build().also {
                it.setSurfaceProvider(vistaPrevia.surfaceProvider)
            }

            try {
                proveedorCamara.unbindAll()
                proveedorCamara.bindToLifecycle(
                    cicloVidaOwner,
                    selectorCamara,
                    vistaPreviaConfigurada,
                    capturaImagen
                )
            } catch (exc: Exception) {
                Log.e("VaultNotes", "Fallo al iniciar cámara", exc)
            }
        }, ejecutor)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { vistaPrevia }, modifier = Modifier.fillMaxSize())
        
        Button(
            onClick = {
                tomarFoto(capturaImagen, contexto, ejecutor, alCapturarFoto)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
        ) {
            Text("Capturar Foto Segura")
        }
    }
}

private fun tomarFoto(
    capturaImagen: ImageCapture,
    contexto: Context,
    ejecutor: Executor,
    alCapturarFoto: (Uri) -> Unit
) {
    val nombreArchivo = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
        .format(System.currentTimeMillis()) + ".jpg"
    
    val archivoSalida = File(contexto.filesDir, nombreArchivo)
    
    val opcionesSalida = ImageCapture.OutputFileOptions.Builder(archivoSalida).build()

    capturaImagen.takePicture(
        opcionesSalida,
        ejecutor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(resultado: ImageCapture.OutputFileResults) {
                val uriGuardada = Uri.fromFile(archivoSalida)
                alCapturarFoto(uriGuardada)
                Log.d("VaultNotes", "Foto guardada en: $uriGuardada")
            }

            override fun onError(exc: ImageCaptureException) {
                Log.e("VaultNotes", "Error al capturar foto", exc)
            }
        }
    )
}
