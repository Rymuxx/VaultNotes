package com.example.vaultnotes.ui.auth

import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

@Composable
fun PantallaLogin(alLoguearExitosamente: () -> Unit) {
    val contexto = LocalContext.current
    val actividad = contexto as? FragmentActivity
    val ejecutor = remember { ContextCompat.getMainExecutor(contexto) }
    
    val biometricPrompt = remember {
        BiometricPrompt(
            actividad!!,
            ejecutor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(resultado: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(resultado)
                    alLoguearExitosamente()
                }
            }
        )
    }

    val infoPrompt = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Autenticación biométrica")
        .setSubtitle("Ingrese para ver sus notas")
        .setNegativeButtonText("Cancelar")
        .build()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bóveda de Notas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { biometricPrompt.authenticate(infoPrompt) }) {
            Text("Desbloquear con Biometría")
        }
    }
}
