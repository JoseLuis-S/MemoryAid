package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


class ObtenerEventosUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    operator fun invoke(query: String = ""): Flow<List<EventoMemoria>> {
        return if (query.isBlank()) {
            repositorio.obtenerTodosLosEventos()
        } else {
            repositorio.buscarEventos(query)
        }
    }
}