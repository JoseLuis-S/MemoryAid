package com.alberti.memoryaid.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.alberti.memoryaid.R
import com.alberti.memoryaid.app.MainActivity

/**
 * Helper para la gestión y despliegue de notificaciones del sistema.
 * * Centraliza la creación de canales de notificación (obligatorios desde Android 8.0)
 * y la construcción de objetos [NotificationCompat.Builder] para estandarizar el
 * comportamiento de las alertas en la aplicación.
 */
object NotificationHelper {

    private const val CHANNEL_ID = "memoryaid_reminders_channel"
    private const val CHANNEL_NAME = "Recordatorios de Cuidado"
    private const val CHANNEL_DESC = "Notificaciones para medicación, crisis y tareas diarias."

    /**
     * Muestra una notificación push con la configuración estándar de la aplicación.
     * * Implementa la creación automática del canal de notificación si el dispositivo
     * ejecuta Android Oreo (API 26) o superior. Al pulsar la notificación, se redirige
     * al usuario a la [MainActivity].
     *
     * @param context Contexto necesario para acceder a los servicios del sistema.
     * @param id Identificador único de la notificación (permite actualizar o cancelar alertas específicas).
     * @param titulo Encabezado principal de la notificación.
     * @param contenido Cuerpo del mensaje detallado.
     */
    fun mostrarNotificacion(
        context: Context,
        id: Int,
        titulo: String,
        contenido: String
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Configuración del canal para API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Intent para abrir la app al tocar la notificación
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construcción de la notificación
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_small) // Asegúrate de que este recurso exista
            .setContentTitle(titulo)
            .setContentText(contenido)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(id, builder.build())
    }
}
