package com.alberti.memoryaid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alberti.memoryaid.data.local.entity.EventoMemoriaEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) para la gestión de persistencia de eventos de memoria.
 * Proporciona métodos para realizar operaciones CRUD y consultas especializadas
 * sobre la tabla `eventos_memoria`.
 */
@Dao
interface EventoMemoriaDao {

    /**
     * Obtiene todos los eventos almacenados, ordenados por fecha y hora de forma descendente.
     * @return [Flow] que emite la lista actualizada de [EventoMemoriaEntity].
     */
    @Query("SELECT * FROM eventos_memoria ORDER BY fechaHora DESC")
    fun obtenerTodos(): Flow<List<EventoMemoriaEntity>>

    /**
     * Filtra eventos por su categoría o tipo.
     * @param tipo El identificador del tipo de evento a filtrar.
     * @return [Flow] con la lista de eventos que coinciden con el [tipo].
     */
    @Query("SELECT * FROM eventos_memoria WHERE tipo = :tipo ORDER BY fechaHora DESC")
    fun obtenerPorTipo(tipo: String): Flow<List<EventoMemoriaEntity>>

    /**
     * Recupera eventos dentro de un rango temporal específico.
     * @param inicio Timestamp inicial en milisegundos.
     * @param fin Timestamp final en milisegundos.
     * @return [Flow] con los eventos ocurridos entre [inicio] y [fin].
     */
    @Query("SELECT * FROM eventos_memoria WHERE fechaHora BETWEEN :inicio AND :fin ORDER BY fechaHora DESC")
    fun obtenerPorRango(inicio: Long, fin: Long): Flow<List<EventoMemoriaEntity>>

    /**
     * Busca un evento específico mediante su identificador único.
     * @param id Identificador de la base de datos.
     * @return El [EventoMemoriaEntity] encontrado o `null` si no existe.
     */
    @Query("SELECT * FROM eventos_memoria WHERE id = :id")
    suspend fun obtenerPorId(id: Long): EventoMemoriaEntity?

    /**
     * Inserta un nuevo evento o reemplaza uno existente si hay conflicto de ID.
     * @param evento La entidad a persistir.
     * @return El ID de la fila recién insertada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(evento: EventoMemoriaEntity): Long

    /**
     * Elimina un evento específico de la base de datos.
     * @param evento La entidad a eliminar.
     */
    @Delete
    suspend fun eliminar(evento: EventoMemoriaEntity)

    /**
     * Realiza una búsqueda avanzada filtrando opcionalmente por tipo y por coincidencia de texto
     * en el título o la descripción.
     * @param tipo Tipo de evento (opcional, puede ser nulo para ignorar el filtro).
     * @param query Cadena de texto a buscar en título o descripción.
     * @return [Flow] con los resultados que cumplen ambos criterios.
     */
    @Query("""
            SELECT * FROM eventos_memoria 
            WHERE (:tipo IS NULL OR tipo = :tipo) 
            AND (titulo LIKE '%' || :query || '%' OR descripcion LIKE '%' || :query || '%')
            ORDER BY fechaHora DESC
        """)
    fun obtenerEventosFiltrados(tipo: String?, query: String): Flow<List<EventoMemoriaEntity>>

    /**
     * Cuenta la cantidad de eventos de un tipo específico desde una fecha determinada.
     * @param tipo Categoría a contabilizar.
     * @param desde Timestamp inicial de corte.
     * @return Total de registros encontrados.
     */
    @Query("SELECT COUNT(*) FROM eventos_memoria WHERE tipo = :tipo AND fechaHora >= :desde")
    suspend fun contarEventosPorTipo(tipo: String, desde: Long): Int

    /**
     * Cuenta el total de eventos registrados desde una fecha determinada.
     * @param desde Timestamp inicial de corte.
     * @return Cantidad total de registros.
     */
    @Query("SELECT COUNT(*) FROM eventos_memoria WHERE fechaHora >= :desde")
    suspend fun contarTotalEventos(desde: Long): Int

    /**
     * Cuenta eventos de un tipo específico dentro de un rango de tiempo.
     * @param tipo Categoría a contabilizar.
     * @param desde Timestamp inicial.
     * @param hasta Timestamp final.
     * @return Total de registros encontrados en el intervalo.
     */
    @Query("SELECT COUNT(*) FROM eventos_memoria WHERE tipo = :tipo AND fechaHora BETWEEN :desde AND :hasta")
    suspend fun contarPorTipoEnRango(tipo: String, desde: Long, hasta: Long): Int

    /**
     * Cuenta el total de eventos registrados dentro de un rango de tiempo.
     * @param desde Timestamp inicial.
     * @param hasta Timestamp final.
     * @return Cantidad total de registros en el intervalo.
     */
    @Query("SELECT COUNT(*) FROM eventos_memoria WHERE fechaHora BETWEEN :desde AND :hasta")
    suspend fun contarTotalEnRango(desde: Long, hasta: Long): Int

    /**
     * Elimina todos los registros de la tabla `eventos_memoria`.
     * Operación destructiva.
     */
    @Query("DELETE FROM eventos_memoria")
    suspend fun borrarTodosLosEventos()

    /**
     * Actualiza los datos de un evento existente.
     * @param evento Entidad con los nuevos valores (debe contener el ID original).
     */
    @Update
    suspend fun actualizar(evento: EventoMemoriaEntity)
}
