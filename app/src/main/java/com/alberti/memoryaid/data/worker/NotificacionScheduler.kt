package com.alberti.memoryaid.data.worker

import android.content.Context
import androidx.work.*
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Orquestador de notificaciones y tareas en segundo plano mediante [WorkManager].
 *
 * Centraliza la lógica para programar, actualizar y cancelar recordatorios únicos,
 * periódicos y resúmenes diarios del sistema.
 */
object NotificacionScheduler {

    /** Clave para el título de la notificación en el [Data] del worker. */
    const val KEY_TITULO = "key_titulo"
    /** Clave para el cuerpo/contenido de la notificación en el [Data] del worker. */
    const val KEY_CONTENIDO = "key_contenido"
    /** Clave para el identificador único del evento asociado. */
    const val KEY_ID = "key_id"

    /**
     * Programa una notificación única para un evento específico tras un retardo determinado.
     *
     * @param context Contexto de la aplicación.
     * @param eventoId Identificador del evento para garantizar la unicidad del trabajo.
     * @param titulo Texto de cabecera de la notificación.
     * @param contenido Detalle de la notificación.
     * @param delayMillis Tiempo de espera en milisegundos antes de ejecutar el worker.
     */
    fun programarRecordatorioUnico(
        context: Context,
        eventoId: Long,
        titulo: String,
        contenido: String,
        delayMillis: Long
    ) {
        val data = workDataOf(
            KEY_ID to eventoId,
            KEY_TITULO to titulo,
            KEY_CONTENIDO to contenido
        )

        val request = OneTimeWorkRequestBuilder<RecordatorioWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("recordatorio_$eventoId")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "work_$eventoId",
            ExistingWorkPolicy.REPLACE, // Reemplaza si ya existe uno pendiente para este ID
            request
        )
    }

    /**
     * Programa una notificación recurrente para un evento.
     *
     * @param context Contexto de la aplicación.
     * @param eventoId Identificador único del evento.
     * @param titulo Texto de cabecera.
     * @param contenido Detalle de la notificación.
     * @param intervaloHoras Frecuencia de repetición (mínimo permitido por WorkManager: 15 min).
     */
    fun programarRecordatorioPeriodico(
        context: Context,
        eventoId: Long,
        titulo: String,
        contenido: String,
        intervaloHoras: Long
    ) {
        val data = workDataOf(
            KEY_ID to eventoId,
            KEY_TITULO to titulo,
            KEY_CONTENIDO to contenido
        )

        val request = PeriodicWorkRequestBuilder<RecordatorioWorker>(intervaloHoras, TimeUnit.HOURS)
            .setInputData(data)
            .addTag("periodico_$eventoId")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "periodic_work_$eventoId",
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    /**
     * Cancela todas las tareas (únicas y periódicas) asociadas a un ID de evento.
     *
     * @param context Contexto de la aplicación.
     * @param eventoId ID del evento cuyas tareas deben ser eliminadas de la cola.
     */
    fun cancelarRecordatorio(context: Context, eventoId: Long) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork("work_$eventoId")
        workManager.cancelUniqueWork("periodic_work_$eventoId")
        workManager.cancelAllWorkByTag("recordatorio_$eventoId")
    }

    /**
     * Programa una tarea diaria para mostrar un resumen de los eventos de la jornada.
     * Se configura para ejecutarse cada 24 horas, idealmente a las 21:00 PM.
     *
     * @param context Contexto de la aplicación.
     */
    fun programarResumenDiario(context: Context) {
        val request = PeriodicWorkRequestBuilder<ResumenDiarioWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(calcularDelayHastaLas21PM(), TimeUnit.MILLISECONDS)
            .addTag("resumen_diario")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "resumen_diario_work",
            ExistingPeriodicWorkPolicy.KEEP, // Mantiene la programación actual si ya existe
            request
        )
    }

    /**
     * Calcula el tiempo restante entre el momento actual y las 21:00 del día de hoy
     * (o de mañana si ya han pasado las 21:00).
     *
     * @return Diferencia en milisegundos.
     */
    private fun calcularDelayHastaLas21PM(): Long {
        val ahora = Calendar.getInstance()
        val proximaEjecucion = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        if (proximaEjecucion.before(ahora)) {
            proximaEjecucion.add(Calendar.DAY_OF_MONTH, 1)
        }
        return proximaEjecucion.timeInMillis - ahora.timeInMillis
    }
}
