package com.alberti.memoryaid.ui.home

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento

data class HomeUiState(
    val eventos: List<EventoMemoria> = emptyList(),
    val estaCargando: Boolean = false,
    val filtroSeleccionado: TipoEvento? = null,
    val mensajeError: String? = null,
    val mostrarDialogoPin: Boolean = false,
    val pinInput: String = "",
    val errorPin: String? = null,
    val navegarAAdmin: Boolean = false,
    val mostrarDialogoConfigContacto: Boolean = false
)
