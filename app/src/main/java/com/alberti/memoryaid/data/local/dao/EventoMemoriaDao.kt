package com.alberti.memoryaid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alberti.memoryaid.data.local.entity.EventoMemoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoMemoriaDao {
    @Query("SELECT * FROM eventos_memoria ORDER BY fechaHora DESC")
    fun obtenerTodos(): Flow<List<EventoMemoriaEntity>>

    @Query("SELECT * FROM eventos_memoria WHERE tipo = :tipo ORDER BY fechaHora DESC")
    fun obtenerPorTipo(tipo: String): Flow<List<EventoMemoriaEntity>>

    @Query("SELECT * FROM eventos_memoria WHERE fechaHora BETWEEN :inicio AND :fin ORDER BY fechaHora DESC")
    fun obtenerPorRango(inicio: Long, fin: Long): Flow<List<EventoMemoriaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(evento: EventoMemoriaEntity)

    @Delete
    suspend fun eliminar(evento: EventoMemoriaEntity)

    @Query("""
        SELECT * FROM eventos_memoria 
        WHERE titulo LIKE '%' || :query || '%' 
        OR descripcion LIKE '%' || :query || '%' 
        ORDER BY fechaHora DESC
    """)
    fun buscarEventos(query: String): Flow<List<EventoMemoriaEntity>>
}