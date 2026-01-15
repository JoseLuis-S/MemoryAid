package com.alberti.memoryaid.data.repository

import com.alberti.memoryaid.data.local.dao.EventoMemoriaDao
import com.alberti.memoryaid.data.mapper.toDomain
import com.alberti.memoryaid.data.mapper.toEntity
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositorioMemoriaImpl @Inject constructor (
    private val dao: EventoMemoriaDao
) : RepositorioMemoria {

    override fun obtenerTodosLosEventos(): Flow<List<EventoMemoria>> {
        return dao.obtenerTodos().map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override fun obtenerEventosPorTipo(tipo: TipoEvento): Flow<List<EventoMemoria>> {
        return dao.obtenerPorTipo(tipo.name).map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override fun obtenerEventosPorRango(inicio: Long, fin: Long): Flow<List<EventoMemoria>> {
        return dao.obtenerPorRango(inicio, fin).map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override suspend fun guardarEvento(evento: EventoMemoria) {
        dao.insertar(evento.toEntity())
    }

    override suspend fun eliminarEvento(evento: EventoMemoria) {
        dao.eliminar(evento.toEntity())
    }

    override fun obtenerEventos(tipo: TipoEvento?, query: String): Flow<List<EventoMemoria>> {
        return dao.obtenerEventosFiltrados(tipo?.name, query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun contarPorTipo(tipo: TipoEvento, desde: Long, hasta: Long): Int {
        return dao.contarPorTipoEnRango(tipo.name, desde, hasta)
    }

    override suspend fun contarTotal(desde: Long, hasta: Long): Int {
        return dao.contarTotalEnRango(desde, hasta)
    }
}
