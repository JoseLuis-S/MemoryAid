package com.alberti.memoryaid.ui.registro

import com.alberti.memoryaid.domain.model.TipoEvento

data class RegistroUiState(
    val id: Long? = null,
    val titulo: String = "",
    val descripcion: String = "",
    val fechaHora: Long = System.currentTimeMillis(),
    val tipo: TipoEvento? = null,
    val recordatorioActivo: Boolean = false,
    val fechaRecordatorio: Long? = null,
    val frecuenciaHoras: Int = 0,
    val esEdicion: Boolean = false,
    val estaGuardando: Boolean = false,
    val registroExitoso: Boolean = false,
    val mostrarAnimacionExito: Boolean = false,
    val error: String? = null,
    val mostrarDatePicker: Boolean = false,
    val mostrarTimePicker: Boolean = false,
    val pinInput: String = ""
)
