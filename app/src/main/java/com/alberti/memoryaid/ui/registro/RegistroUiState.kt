package com.alberti.memoryaid.ui.registro

import com.alberti.memoryaid.domain.model.TipoEvento

data class RegistroUiState(
    val id: Long? = null,
    val titulo: String = "",
    val descripcion: String = "",
    val fechaHora: Long = System.currentTimeMillis(),
    val tipo: TipoEvento? = null,
    val esEdicion: Boolean = false,
    val estaGuardando: Boolean = false,
    val registroExitoso: Boolean = false,
    val error: String? = null,
    val recordatorioActivo: Boolean = false,
    val fechaRecordatorio: Long? = null,
    val frecuenciaHoras: Int = 0,
    val mostrarDatePicker: Boolean = false,
    val mostrarTimePicker: Boolean = false
)
