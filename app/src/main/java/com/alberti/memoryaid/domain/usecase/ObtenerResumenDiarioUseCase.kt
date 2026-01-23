package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Caso de uso encargado de generar una métrica agrupada de los eventos ocurridos en un día específico.
 * * Transforma un flujo de eventos en un mapa estadístico que contabiliza la frecuencia de cada
 * [TipoEvento]. Es ideal para componentes de UI que muestran resúmenes diarios o dashboards rápidos.
 *
 * @property repositorio Fuente de datos para la consulta de eventos por rango temporal.
 */
class ObtenerResumenDiarioUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    /**
     * Ejecuta la lógica de agrupación y conteo para la fecha proporcionada.
     * * Calcula los límites temporales (00:00:00 a 23:59:59) del día solicitado y transforma
     * la lista resultante en un mapa de frecuencias.
     *
     * @param fecha Marca de tiempo (ms) perteneciente al día que se desea resumir.
     * @return [Flow] que emite un mapa donde la clave es el [TipoEvento] y el valor es la
     * cantidad de registros encontrados para ese tipo en el día.
     */
    operator fun invoke(fecha: Long): Flow<Map<TipoEvento, Int>> {
        val inicioDia = calcularInicioDia(fecha)
        val finDia = inicioDia + 86_400_000 // Duración exacta de 24 horas en milisegundos

        return repositorio.obtenerEventosPorRango(inicioDia, finDia).map { lista ->
            lista.groupBy { it.tipo ?: TipoEvento.NOTAS_GENERALES }
                .mapValues { it.value.size }
        }
    }

    /**
     * Normaliza un timestamp al inicio del día (medianoche UTC).
     * * Utiliza aritmética de restos para eliminar la precisión de horas, minutos y segundos.
     * * @param timestamp Marca de tiempo original.
     * @return Timestamp correspondiente a las 00:00:00 del mismo día.
     */
    private fun calcularInicioDia(timestamp: Long): Long {
        return timestamp - (timestamp % 86_400_000)
    }
}
