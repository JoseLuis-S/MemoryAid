package com.alberti.memoryaid.ui.admin

import com.alberti.memoryaid.domain.model.EstadisticasAdmin

data class AdminUiState(
    val estaCargando: Boolean = false,
    val estadisticas: EstadisticasAdmin = EstadisticasAdmin(),
    val mensajeError: String? = null,
    val operacionExitosa: Boolean = false
)
