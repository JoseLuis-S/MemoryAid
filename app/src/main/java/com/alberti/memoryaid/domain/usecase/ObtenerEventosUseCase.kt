package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


class ObtenerEventosUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    operator fun invoke(tipo: TipoEvento?, query: String): Flow<List<EventoMemoria>> {
        return repositorio.obtenerEventos(tipo, query)
    }
}