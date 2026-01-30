package com.example.vaultnotes

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val contexto = LocalContext.current
            VaultNotesTheme {
                val controladorNavegacion = rememberNavController()
                
                var tienePermisoCamara by remember {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            contexto,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                }

                val lanzadorPermisos = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { concedido ->
                    tienePermisoCamara = concedido
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
                        PantallaNotas(alAbrirCamara = {
                            if (tienePermisoCamara) {
                                controladorNavegacion.navigate("camara")
                            } else {
                                lanzadorPermisos.launch(Manifest.permission.CAMERA)
                            }
                        })
                    }
                    composable("camara") {
                        PantallaCamara(alCapturarFoto = { uri ->
                            Toast.makeText(contexto, "Foto guardada en boveda", Toast.LENGTH_SHORT).show()
                            controladorNavegacion.popBackStack()
                        })
                    }
                }
            }
        }
    }
}
