package com.example.vaultnotes.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.vaultnotes.data.repository.RepositorioSeguridad
import java.io.File

class TrabajadorRespaldoSeguro(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val seguridad = RepositorioSeguridad()

    override suspend fun doWork(): Result {
        return try {
            // 1. Obtenemos las notas (aquí conectarías con tu DB)
            val datosSensibles = "Lista de notas: [Nota 1, Nota 2...]"
            
            // 2. Encriptamos TODO el paquete antes de que toque el disco
            val respaldoEncriptado = seguridad.encriptar(datosSensibles)
            
            // 3. Escribimos el archivo en el almacenamiento privado
            val archivoRespaldo = File(applicationContext.filesDir, "respaldo_boveda.dat")
            archivoRespaldo.writeText(respaldoEncriptado)
            
            Result.success()
        } catch (e: Exception) {
            Result.retry() // Reintenta si el sistema está ocupado
        }
    }
}
