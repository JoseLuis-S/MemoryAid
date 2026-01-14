package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObtenerTimelineUseCase @Inject constructor (private val repositorio: RepositorioMemoria) {
    operator fun invoke(): Flow<List<EventoMemoria>> {
        return repositorio.obtenerTodosLosEventos()
    }
}