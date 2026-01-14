package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject

class EliminarEventoUseCase @Inject constructor (private val repositorio: RepositorioMemoria) {
    suspend operator fun invoke(evento: EventoMemoria) {
        repositorio.eliminarEvento(evento)
    }
}