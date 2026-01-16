package com.alberti.memoryaid.ui.admin

import com.alberti.memoryaid.domain.model.EstadisticasAdmin

data class AdminUiState(
    val estadisticas: EstadisticasAdmin = EstadisticasAdmin(0, 0, 0, ""),
    val estaCargando: Boolean = false,
    val mensajeError: String? = null,
    val mostrarConfirmacionPurga: Boolean = false,
    val informeGenerado: String? = null,
    val mostrarDialogoPin: Boolean = false,
    val nuevoPinInput: String = "",
    val mostrarDialogoEmergencia: Boolean = false,
    val nuevoEmergenciaInput: String = "",
    val errorValidacion: String? = null
)