package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso encargado de la búsqueda y filtrado dinámico de eventos de memoria.
 * * Este componente permite recuperar un flujo de datos que se actualiza en tiempo real
 * basándose en criterios opcionales de categoría (tipo) y coincidencias de texto.
 *
 * @property repositorio Interfaz del repositorio para acceder a la fuente de datos.
 */
class ObtenerEventosUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    /**
     * Ejecuta la consulta de eventos aplicando filtros.
     * * @param tipo Categoría del evento por la cual filtrar. Si es nulo, se omitirá este filtro.
     * @param query Cadena de texto para buscar coincidencias en el título o descripción del evento.
     * @return [Flow] que emite una lista de [EventoMemoria] cada vez que los datos subyacentes cambien.
     */
    operator fun invoke(tipo: TipoEvento?, query: String): Flow<List<EventoMemoria>> {
        return repositorio.obtenerEventos(tipo, query)
    }
}
