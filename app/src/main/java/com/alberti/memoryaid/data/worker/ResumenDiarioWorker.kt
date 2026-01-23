package com.alberti.memoryaid.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.alberti.memoryaid.R
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.usecase.ObtenerResumenDiarioUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

/**
 * Worker encargado de generar y mostrar un resumen diario de las actividades.
 * * Utiliza [CoroutineWorker] para ejecutar operaciones asíncronas de forma segura
 * e integra **Hilt** para la inyección de dependencias en tareas de fondo.
 *
 * @param context Contexto de la aplicación inyectado por WorkManager.
 * @param workerParams Parámetros de ejecución inyectados por WorkManager.
 * @param obtenerResumenDiarioUseCase Caso de uso para recuperar las estadísticas de eventos del día.
 */
@HiltWorker
class ResumenDiarioWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val obtenerResumenDiarioUseCase: ObtenerResumenDiarioUseCase
) : CoroutineWorker(context, workerParams) {

    /**
     * Ejecuta la lógica de recolección de datos y notificación.
     * * Recupera el primer valor del [Flow] proporcionado por el caso de uso y,
     * si existen datos, dispara la notificación del sistema.
     *
     * @return [Result.success] tras completar el procesamiento.
     */
    override suspend fun doWork(): Result {
        val hoy = System.currentTimeMillis()

        // Obtenemos el resumen actual (consumo one-shot del Flow)
        val resumen = obtenerResumenDiarioUseCase(hoy).first()

        if (resumen.isNotEmpty()) {
            mostrarNotificacion(resumen)
        }

        return Result.success()
    }

    /**
     * Construye y despliega la notificación con el desglose por tipo de evento.
     * * Crea el canal de notificación específico para resúmenes si es necesario.
     *
     * @param resumen Mapa que contiene el conteo de eventos agrupados por [TipoEvento].
     */
    private fun mostrarNotificacion(resumen: Map<TipoEvento, Int>) {
        val channelId = "resumen_diario_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Configuración del canal para Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Resumen Diario",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Formateo del cuerpo del mensaje: "TIPO: CANTIDAD"
        val textoResumen = resumen.entries.joinToString { "${it.key.name}: ${it.value}" }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Sugerido: ic_notification_summary
            .setContentTitle("Resumen de Actividad de Hoy")
            .setContentText(textoResumen)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
