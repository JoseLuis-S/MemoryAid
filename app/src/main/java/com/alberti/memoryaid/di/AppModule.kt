package com.alberti.memoryaid.di

import android.content.Context
import com.alberti.memoryaid.data.local.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

/**
 * Módulo de Hilt encargado de proveer dependencias de alcance global (Singleton).
 * * Se instala en [SingletonComponent], lo que garantiza que las instancias provistas
 * vivan durante todo el ciclo de vida de la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provee una instancia única de [SessionManager].
     * * @param context El contexto de la aplicación, inyectado automáticamente mediante
     * la anotación [@ApplicationContext] para evitar memory leaks.
     * @return Una instancia de [SessionManager] configurada para la persistencia de preferencias.
     */
    @Provides
    @Singleton
    fun provideSessionManager(
        @ApplicationContext context: Context
    ): SessionManager = SessionManager(context)
}
