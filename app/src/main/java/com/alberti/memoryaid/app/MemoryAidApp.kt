package com.alberti.memoryaid.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Punto de entrada base de la aplicación.
 * * Configura el grafo de dependencias de Hilt a nivel global mediante la anotación
 * [HiltAndroidApp]. Implementa [Configuration.Provider] para permitir la inyección
 * de dependencias dentro de los Workers de WorkManager.
 */
@HiltAndroidApp
class MemoryAidApp : Application(), Configuration.Provider {

    /**
     * Factoría de Workers proporcionada por Hilt.
     * Permite que WorkManager instancie Workers que requieren dependencias
     * inyectadas en sus constructores.
     */
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    /**
     * Define la configuración personalizada de WorkManager.
     * * Utiliza la [workerFactory] inyectada para delegar la creación de tareas
     * en segundo plano al grafo de dependencias de Hilt.
     */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
