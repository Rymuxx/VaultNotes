package com.example.vaultnotes.ui.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vaultnotes.viewmodel.NotasViewModel

@Composable
fun PantallaNotas(viewModel: NotasViewModel = viewModel()) {
    var estadoTexto by remember { mutableStateOf("") }
    val notas by viewModel.notas.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                if (estadoTexto.isNotBlank()) viewModel.agregarNota(estadoTexto)
                estadoTexto = ""
            }) { Icon(Icons.Default.Add, "Guardar") }
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
                        Text(nota.contenido, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}
