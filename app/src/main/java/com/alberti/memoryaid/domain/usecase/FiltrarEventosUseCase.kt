package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class FiltrarEventosUseCase @Inject constructor (private val repositorio: RepositorioMemoria) {
    operator fun invoke(tipo: TipoEvento?): Flow<List<EventoMemoria>> {
        return if (tipo == null) {
            repositorio.obtenerTodosLosEventos()
        } else {
            repositorio.obtenerEventosPorTipo(tipo)
        }
    }
}