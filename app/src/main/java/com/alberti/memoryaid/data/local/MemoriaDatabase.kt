package com.alberti.memoryaid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alberti.memoryaid.data.local.dao.EventoMemoriaDao
import com.alberti.memoryaid.data.local.entity.EventoMemoriaEntity

@Database(
    entities = [EventoMemoriaEntity::class],
    version = 2,
    exportSchema = false
)
abstract class MemoriaDatabase : RoomDatabase() {
    abstract fun eventoDao(): EventoMemoriaDao
}
