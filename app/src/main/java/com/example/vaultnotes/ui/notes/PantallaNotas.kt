package com.example.vaultnotes.ui.notes

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.vaultnotes.viewmodel.NotasViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PantallaNotas(
    viewModel: NotasViewModel = viewModel(),
    alAbrirCamara: () -> Unit
) {
    var estadoTexto by remember { mutableStateOf("") }
    val notas by viewModel.notas.collectAsState()
    val scope = rememberCoroutineScope()
    
    var presionado by remember { mutableStateOf(false) }
    val escala by animateFloatAsState(
        targetValue = if (presionado) 0.8f else 1f,
        animationSpec = spring(dampingRatio = 0.4f),
        label = "AnimacionBoton"
    )

    Scaffold(
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = alAbrirCamara,
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Default.CameraAlt, "Tomar Foto")
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            presionado = true
                            if (estadoTexto.isNotBlank()) {
                                viewModel.agregarNota(estadoTexto)
                                estadoTexto = ""
                            }
                            delay(100)
                            presionado = false
                        }
                    },
                    modifier = Modifier.scale(escala)
                ) {
                    Icon(Icons.Default.Add, "Guardar Nota")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            TextField(
                value = estadoTexto,
                onValueChange = { estadoTexto = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nota secreta...") }
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                items(notas) { nota ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (nota.contenido != null) {
                                Text(nota.contenido)
                            }
                            if (nota.uriImagen != null) {
                                AsyncImage(
                                    model = nota.uriImagen,
                                    contentDescription = "Imagen capturada",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
