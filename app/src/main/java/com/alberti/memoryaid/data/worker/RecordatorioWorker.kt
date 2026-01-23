package com.alberti.memoryaid.data.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.alberti.memoryaid.data.worker.NotificacionScheduler.KEY_CONTENIDO
import com.alberti.memoryaid.data.worker.NotificacionScheduler.KEY_ID
import com.alberti.memoryaid.data.worker.NotificacionScheduler.KEY_TITULO

/**
 * Worker encargado de ejecutar la lógica de notificación en segundo plano.
 * * Este componente es instanciado por [androidx.work.WorkManager] cuando se cumplen
 * las condiciones de tiempo programadas. Recupera los datos del evento desde los
 * parámetros de entrada y delega el despliegue de la UI a [NotificationHelper].
 *
 * @param context El contexto de la aplicación proporcionado por WorkManager.
 * @param params Parámetros de configuración y datos de entrada para la tarea.
 */
class RecordatorioWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    /**
     * Ejecuta la tarea de notificación.
     * * Extrae el título, contenido e ID del evento desde [getInputData]. Si faltan datos,
     * utiliza valores por defecto para evitar fallos críticos.
     *
     * @return [Result.success] si la notificación se procesó correctamente,
     * o [Result.failure] si ocurrió una excepción inesperada.
     */
    override fun doWork(): Result {
        return try {
            // Extracción de datos usando las constantes del Scheduler
            val titulo = inputData.getString(KEY_TITULO) ?: "Recordatorio"
            val contenido = inputData.getString(KEY_CONTENIDO) ?: ""
            val id = inputData.getLong(KEY_ID, 0L)

            NotificationHelper.mostrarNotificacion(
                context = applicationContext,
                id = id.toInt(),
                titulo = titulo,
                contenido = contenido
            )

            Result.success()
        } catch (e: Exception) {
            // Podrías loguear la excepción aquí antes de retornar failure
            Result.failure()
        }
    }
}
