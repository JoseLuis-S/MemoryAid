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

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MemoriaDatabase {
        return Room.databaseBuilder(
            context,
            MemoriaDatabase::class.java,
            "memoria_db"
        ).build()
    }

    @Provides
    fun provideEventoDao(db: MemoriaDatabase): EventoMemoriaDao {
        return db.eventoDao()
    }
}