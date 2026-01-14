package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObtenerResumenDiarioUseCase @Inject constructor (private val repositorio: RepositorioMemoria) {
    operator fun invoke(fecha: Long): Flow<Map<TipoEvento, Int>> {
        val inicioDia = calcularInicioDia(fecha)
        val finDia = inicioDia + 86_400_000 // +24h

        return repositorio.obtenerEventosPorRango(inicioDia, finDia).map { lista ->
            lista.groupBy { it.tipo ?: TipoEvento.NOTAS_GENERALES }
                .mapValues { it.value.size }
        }
    }

    private fun calcularInicioDia(timestamp: Long): Long {
        return timestamp - (timestamp % 86_400_000)
    }
}