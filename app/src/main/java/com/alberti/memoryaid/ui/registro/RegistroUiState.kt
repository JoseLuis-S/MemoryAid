package com.alberti.memoryaid.ui.registro

import com.alberti.memoryaid.domain.model.TipoEvento

data class RegistroUiState(
    val id: Long? = null,
    val titulo: String = "",
    val descripcion: String = "",
    val fechaHora: Long = System.currentTimeMillis(),
    val tipo: TipoEvento? = null,
    val estaGuardando: Boolean = false,
    val registroExitoso: Boolean = false,
    val error: String? = null,
    val esEdicion: Boolean = false
)
