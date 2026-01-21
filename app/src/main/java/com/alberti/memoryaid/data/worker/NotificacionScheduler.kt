package com.alberti.memoryaid.data.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit
import androidx.work.*

object NotificacionScheduler {

    const val KEY_TITULO = "key_titulo"
    const val KEY_CONTENIDO = "key_contenido"
    const val KEY_ID = "key_id"

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
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

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

    fun cancelarRecordatorio(context: Context, eventoId: Long) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork("work_$eventoId")
        workManager.cancelUniqueWork("periodic_work_$eventoId")
        workManager.cancelAllWorkByTag("recordatorio_$eventoId")
    }

    fun programarResumenDiario(context: Context) {
        val request = PeriodicWorkRequestBuilder<ResumenDiarioWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(calcularDelayHastaLas21PM(), TimeUnit.MILLISECONDS)
            .addTag("resumen_diario")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "resumen_diario_work",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

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
