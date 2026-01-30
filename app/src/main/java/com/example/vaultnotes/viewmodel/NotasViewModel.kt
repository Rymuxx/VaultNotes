package com.example.vaultnotes.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Nota(
    val id: Int, 
    val contenido: String? = null, 
    val uriImagen: Uri? = null
)

class NotasViewModel : ViewModel() {
    private val _notas = MutableStateFlow<List<Nota>>(emptyList())
    val notas = _notas.asStateFlow()

    fun agregarNota(texto: String) {
        val nuevaNota = Nota(id = _notas.value.size, contenido = texto)
        _notas.value = _notas.value + nuevaNota
    }

    fun agregarFoto(uri: Uri) {
        val nuevaNota = Nota(id = _notas.value.size, uriImagen = uri)
        _notas.value = _notas.value + nuevaNota
    }
}
