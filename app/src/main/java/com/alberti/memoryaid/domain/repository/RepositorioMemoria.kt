package com.alberti.memoryaid.domain.repository

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import kotlinx.coroutines.flow.Flow

interface RepositorioMemoria {
    suspend fun guardarEvento(evento: EventoMemoria)
    suspend fun eliminarEvento(evento: EventoMemoria)
    fun obtenerTodosLosEventos(): Flow<List<EventoMemoria>>
    fun obtenerEventosPorTipo(tipo: TipoEvento): Flow<List<EventoMemoria>>
    fun obtenerEventosPorRango(inicio: Long, fin: Long): Flow<List<EventoMemoria>>
    fun obtenerEventos(tipo: TipoEvento?, query: String): Flow<List<EventoMemoria>>
    suspend fun contarPorTipo(tipo: TipoEvento, desde: Long, hasta: Long = System.currentTimeMillis()): Int
    suspend fun contarTotal(desde: Long, hasta: Long = System.currentTimeMillis()): Int
    suspend fun borrarTodo()
}