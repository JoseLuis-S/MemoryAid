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

@HiltWorker
class ResumenDiarioWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val obtenerResumenDiarioUseCase: ObtenerResumenDiarioUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val hoy = System.currentTimeMillis()

        val resumen = obtenerResumenDiarioUseCase(hoy).first()

        if (resumen.isNotEmpty()) {
            mostrarNotificacion(resumen)
        }

        return Result.success()
    }

    private fun mostrarNotificacion(resumen: Map<TipoEvento, Int>) {
        val channelId = "resumen_diario_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Resumen Diario", NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val textoResumen = resumen.entries.joinToString { "${it.key.name}: ${it.value}" }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Resumen de Actividad de Hoy")
            .setContentText(textoResumen)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
