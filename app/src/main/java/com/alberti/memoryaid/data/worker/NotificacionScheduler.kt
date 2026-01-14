package com.alberti.memoryaid.data.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificacionScheduler {
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