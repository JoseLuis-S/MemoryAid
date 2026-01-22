package com.alberti.memoryaid.ui.admin

import com.alberti.memoryaid.domain.model.EstadisticasAdmin

data class AdminUiState(
    val estaCargando: Boolean = false,
    val estadisticas: EstadisticasAdmin = EstadisticasAdmin(0, 0, 0, ""),
    val mostrarDialogoPin: Boolean = false,
    val nuevoPinInput: String = "",
    val errorValidacion: String? = null,
    val mostrarDialogoEmergencia: Boolean = false,
    val nuevoEmergenciaInput: String = "",
    val mostrarConfirmacionPurga: Boolean = false,
    val informeGenerado: String? = null,
    val necesitaConfiguracion: Boolean = false
)
