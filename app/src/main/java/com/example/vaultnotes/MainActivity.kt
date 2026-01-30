package com.example.vaultnotes

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vaultnotes.ui.auth.PantallaLogin
import com.example.vaultnotes.ui.notes.PantallaNotas
import com.example.vaultnotes.ui.theme.VaultNotesTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VaultNotesTheme {
                val controladorNavegacion = rememberNavController()
                NavHost(navController = controladorNavegacion, startDestination = "login") {
                    composable("login") {
                        PantallaLogin(alLoguearExitosamente = {
                            controladorNavegacion.navigate("notas") {
                                popUpTo("login") { inclusive = true }
                            }
                        })
                    }
                    composable("notas") { PantallaNotas() }
                }
            }
        }
    }
}
