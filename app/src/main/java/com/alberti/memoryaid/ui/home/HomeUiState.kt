package com.alberti.memoryaid.ui.home

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento

data class HomeUiState(
    val estaCargando: Boolean = false,
    val eventos: List<EventoMemoria> = emptyList(),
    val mensajeError: String? = null,
    val filtroSeleccionado: TipoEvento? = null
)