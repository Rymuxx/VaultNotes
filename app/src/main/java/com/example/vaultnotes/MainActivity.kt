package com.example.vaultnotes

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vaultnotes.ui.auth.PantallaLogin
import com.example.vaultnotes.ui.camera.PantallaCamara
import com.example.vaultnotes.ui.notes.PantallaNotas
import com.example.vaultnotes.ui.theme.VaultNotesTheme
import com.example.vaultnotes.viewmodel.NotasViewModel

class MainActivity : FragmentActivity() {
    private val notasViewModel: NotasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val contexto = LocalContext.current
            VaultNotesTheme {
                val controladorNavegacion = rememberNavController()
                
                val lanzadorPermisos = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { concedido ->
                    if (concedido) {
                        controladorNavegacion.navigate("camara")
                    } else {
                        Toast.makeText(contexto, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
                    }
                }

                NavHost(navController = controladorNavegacion, startDestination = "login") {
                    composable("login") {
                        PantallaLogin(alLoguearExitosamente = {
                            controladorNavegacion.navigate("notas") {
                                popUpTo("login") { inclusive = true }
                            }
                        })
                    }
                    composable("notas") {
                        PantallaNotas(
                            viewModel = notasViewModel,
                            alAbrirCamara = {
                                val estadoPermiso = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA)
                                if (estadoPermiso == PackageManager.PERMISSION_GRANTED) {
                                    controladorNavegacion.navigate("camara")
                                } else {
                                    lanzadorPermisos.launch(Manifest.permission.CAMERA)
                                }
                            }
                        )
                    }
                    composable("camara") {
                        PantallaCamara(alCapturarFoto = { uri ->
                            notasViewModel.agregarFoto(uri)
                            Toast.makeText(contexto, "Foto guardada en boveda", Toast.LENGTH_SHORT).show()
                            controladorNavegacion.popBackStack()
                        })
                    }
                }
            }
        }
    }
}
