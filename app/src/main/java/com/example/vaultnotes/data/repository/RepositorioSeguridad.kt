package com.example.vaultnotes.data.repository

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class RepositorioSeguridad {
    private val generadorLlaves = KeyGenerator.getInstance("AES")
    private val llaveSecreta: SecretKey = generadorLlaves.generateKey()

    fun encriptar(texto: String): String {
        val cifrador = Cipher.getInstance("AES")
        cifrador.init(Cipher.ENCRYPT_MODE, llaveSecreta)
        val encriptado = cifrador.doFinal(texto.toByteArray())
        return Base64.encodeToString(encriptado, Base64.DEFAULT)
    }
}
