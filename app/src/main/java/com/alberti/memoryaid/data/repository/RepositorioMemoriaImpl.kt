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

/**
 * Implementación del repositorio [RepositorioMemoria].
 * * Actúa como mediador entre la fuente de datos local (Room) y la capa de dominio.
 * Se encarga de la orquestación de mappers para asegurar que la lógica de negocio
 * solo consuma modelos de dominio [EventoMemoria].
 *
 * @property dao Instancia del DAO para operaciones sobre la base de datos SQLite.
 */
class RepositorioMemoriaImpl @Inject constructor (
    private val dao: EventoMemoriaDao
) : RepositorioMemoria {

    /**
     * Recupera el flujo continuo de todos los eventos registrados.
     * @return [Flow] con la lista de eventos convertidos a modelos de dominio.
     */
    override fun obtenerTodosLosEventos(): Flow<List<EventoMemoria>> {
        return dao.obtenerTodos().map { lista ->
            lista.map { it.toDomain() }
        }
    }

    /**
     * Filtra eventos por su categoría.
     * @param tipo Constante de [TipoEvento] para el filtrado.
     * @return [Flow] reactivo con los eventos filtrados.
     */
    override fun obtenerEventosPorTipo(tipo: TipoEvento): Flow<List<EventoMemoria>> {
        return dao.obtenerPorTipo(tipo.name).map { lista ->
            lista.map { it.toDomain() }
        }
    }

    /**
     * Obtiene eventos comprendidos en un rango temporal.
     * @param inicio Timestamp inicial.
     * @param fin Timestamp final.
     * @return [Flow] con los eventos resultantes.
     */
    override fun obtenerEventosPorRango(inicio: Long, fin: Long): Flow<List<EventoMemoria>> {
        return dao.obtenerPorRango(inicio, fin).map { lista ->
            lista.map { it.toDomain() }
        }
    }

    /**
     * Inserta un evento en la base de datos persistente.
     * @param evento Modelo de dominio a guardar.
     * @return ID generado para el nuevo registro.
     */
    override suspend fun insertarEvento(evento: EventoMemoria): Long {
        return dao.insertar(evento.toEntity())
    }

    /**
     * Elimina un registro de evento existente.
     * @param evento Modelo de dominio a eliminar.
     */
    override suspend fun eliminarEvento(evento: EventoMemoria) {
        dao.eliminar(evento.toEntity())
    }

    /**
     * Realiza una búsqueda compleja con filtros opcionales de tipo y coincidencia de texto.
     * @param tipo Categoría opcional.
     * @param query Texto de búsqueda para título o descripción.
     * @return [Flow] con los resultados que cumplen los criterios.
     */
    override fun obtenerEventos(tipo: TipoEvento?, query: String): Flow<List<EventoMemoria>> {
        return dao.obtenerEventosFiltrados(tipo?.name, query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Contabiliza eventos de un tipo específico en un intervalo dado.
     * @param tipo Categoría a filtrar.
     * @param desde Límite inferior del tiempo.
     * @param hasta Límite superior del tiempo.
     * @return Cantidad de registros encontrados.
     */
    override suspend fun contarPorTipo(tipo: TipoEvento, desde: Long, hasta: Long): Int {
        return dao.contarPorTipoEnRango(tipo.name, desde, hasta)
    }

    /**
     * Contabiliza el total de eventos en un intervalo dado.
     * @param desde Límite inferior del tiempo.
     * @param hasta Límite superior del tiempo.
     * @return Cantidad total de registros encontrados.
     */
    override suspend fun contarTotal(desde: Long, hasta: Long): Int {
        return dao.contarTotalEnRango(desde, hasta)
    }

    /**
     * Ejecuta una limpieza total de la tabla de eventos.
     */
    override suspend fun borrarTodo() {
        dao.borrarTodosLosEventos()
    }

    /**
     * Actualiza la información de un evento existente.
     * @param evento Modelo con los datos actualizados.
     */
    override suspend fun actualizarEvento(evento: EventoMemoria) {
        dao.actualizar(evento.toEntity())
    }

    /**
     * Recupera un evento único por su identificador.
     * @param id Identificador de la entidad.
     * @return El modelo de dominio encontrado o `null` si no existe.
     */
    override suspend fun obtenerEventoPorId(id: Long): EventoMemoria? {
        return dao.obtenerPorId(id)?.toDomain()
    }
}
