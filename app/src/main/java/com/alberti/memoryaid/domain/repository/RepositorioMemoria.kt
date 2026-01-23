package com.alberti.memoryaid.domain.repository

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import kotlinx.coroutines.flow.Flow

/**
 * Contrato que define las operaciones de persistencia y consulta para los eventos de memoria.
 *
 * Esta interfaz reside en la capa de **Dominio** y debe ser implementada en la capa de **Datos**.
 * Permite un desacoplamiento total de la lógica de negocio respecto a la implementación
 * específica de la base de datos (Inversión de Dependencias).
 */
interface RepositorioMemoria {

    /**
     * Persiste un nuevo evento.
     * @param evento El objeto de dominio a insertar.
     * @return El ID generado para el registro.
     */
    suspend fun insertarEvento(evento: EventoMemoria): Long

    /**
     * Elimina un evento existente.
     * @param evento La entidad de dominio a remover.
     */
    suspend fun eliminarEvento(evento: EventoMemoria)

    /**
     * Observa todos los eventos almacenados.
     * @return [Flow] que emite la lista completa de eventos, ordenada usualmente por fecha.
     */
    fun obtenerTodosLosEventos(): Flow<List<EventoMemoria>>

    /**
     * Filtra los eventos por su categoría.
     * @param tipo El [TipoEvento] a filtrar.
     * @return [Flow] con la lista de eventos que coinciden con el tipo.
     */
    fun obtenerEventosPorTipo(tipo: TipoEvento): Flow<List<EventoMemoria>>

    /**
     * Recupera eventos dentro de un marco temporal específico.
     * @param inicio Timestamp de apertura (milisegundos).
     * @param fin Timestamp de cierre (milisegundos).
     * @return [Flow] con los eventos contenidos en el rango.
     */
    fun obtenerEventosPorRango(inicio: Long, fin: Long): Flow<List<EventoMemoria>>

    /**
     * Realiza una búsqueda avanzada con criterios combinados.
     * @param tipo Categoría opcional (si es nulo, no filtra por tipo).
     * @param query Cadena de texto para buscar coincidencias en título o descripción.
     * @return [Flow] con los resultados filtrados.
     */
    fun obtenerEventos(tipo: TipoEvento?, query: String): Flow<List<EventoMemoria>>

    /**
     * Cuenta la cantidad de eventos de un tipo específico en un periodo de tiempo.
     * @param tipo Categoría a contabilizar.
     * @param desde Timestamp inicial.
     * @param hasta Timestamp final (por defecto es el momento actual).
     * @return Cantidad de registros encontrados.
     */
    suspend fun contarPorTipo(tipo: TipoEvento, desde: Long, hasta: Long = System.currentTimeMillis()): Int

    /**
     * Cuenta el total de eventos registrados en un periodo de tiempo.
     * @param desde Timestamp inicial.
     * @param hasta Timestamp final (por defecto es el momento actual).
     * @return Cantidad total de registros.
     */
    suspend fun contarTotal(desde: Long, hasta: Long = System.currentTimeMillis()): Int

    /**
     * Elimina de forma permanente todos los registros de la fuente de datos.
     */
    suspend fun borrarTodo()

    /**
     * Actualiza la información de un evento previamente guardado.
     * @param evento Objeto con los nuevos datos (debe mantener el ID original).
     */
    suspend fun actualizarEvento(evento: EventoMemoria)

    /**
     * Busca un único evento por su identificador.
     * @param id Identificador único.
     * @return El [EventoMemoria] encontrado o `null` si no existe.
     */
    suspend fun obtenerEventoPorId(id: Long): EventoMemoria?
}
