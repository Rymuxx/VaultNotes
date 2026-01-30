package com.example.vaultnotes.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Nota(val id: Int, val contenido: String)

class NotasViewModel : ViewModel() {
    private val _notas = MutableStateFlow<List<Nota>>(emptyList())
    val notas = _notas.asStateFlow()

    fun agregarNota(texto: String) {
        val nuevaNota = Nota(id = _notas.value.size, contenido = texto)
        _notas.value = _notas.value + nuevaNota
    }
}
