package com.alberti.memoryaid.data.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class RecordatorioWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        return try {
            val titulo = inputData.getString("key_titulo") ?: "Recordatorio"
            val contenido = inputData.getString("key_contenido") ?: ""
            val id = inputData.getLong("key_id", 0L)

            NotificationHelper.mostrarNotificacion(
                context = applicationContext,
                id = id.toInt(),
                titulo = titulo,
                contenido = contenido
            )
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
