package com.alberti.memoryaid.di

import android.content.Context
import androidx.room.Room
import com.alberti.memoryaid.data.local.MemoriaDatabase
import com.alberti.memoryaid.data.local.dao.EventoMemoriaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

/**
 * Módulo de Hilt responsable de la configuración y provisión de la base de datos Room y sus DAOs.
 * * Este módulo se instala en [SingletonComponent], asegurando que tanto la instancia
 * de la base de datos como los DAOs tengan un alcance global durante la ejecución de la app.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Construye y provee la instancia única de [MemoriaDatabase].
     * * Configura el nombre del archivo de base de datos y establece la política de migración.
     * * @param context Contexto de la aplicación inyectado por Hilt.
     * @return Una instancia de la base de datos configurada.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MemoriaDatabase {
        return Room.databaseBuilder(
            context,
            MemoriaDatabase::class.java,
            "memoria_db"
        )
            // Impide la pérdida de datos accidental al requerir migraciones explícitas
            .fallbackToDestructiveMigration(false)
            .build()
    }

    /**
     * Provee el acceso a [EventoMemoriaDao] para su inyección en la capa de datos (Repositorios).
     * * Al depender de la instancia de la base de datos provista por [provideDatabase],
     * se garantiza que todas las operaciones se realicen sobre el mismo hilo y transacción.
     * * @param db Instancia de [MemoriaDatabase] provista por Hilt.
     * @return El DAO encargado de las operaciones sobre la tabla de eventos de memoria.
     */
    @Provides
    fun provideEventoDao(db: MemoriaDatabase): EventoMemoriaDao {
        return db.eventoDao()
    }
}
