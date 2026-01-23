package com.alberti.memoryaid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alberti.memoryaid.data.local.dao.EventoMemoriaDao
import com.alberti.memoryaid.data.local.entity.EventoMemoriaEntity

/**
 * Punto de entrada principal para la persistencia de datos local mediante Room.
 * * Esta clase abstracta define la configuración de la base de datos de la aplicación,
 * incluyendo sus entidades, versión de esquema y el acceso a los DAOs.
 *
 * @see RoomDatabase
 */
@Database(
    entities = [EventoMemoriaEntity::class],
    version = 2,
    exportSchema = false
)
abstract class MemoriaDatabase : RoomDatabase() {

    /**
     * Proporciona acceso al DAO para realizar operaciones sobre los eventos de memoria.
     * * @return Una instancia de [EventoMemoriaDao] gestionada por Room.
     */
    abstract fun eventoDao(): EventoMemoriaDao
}
