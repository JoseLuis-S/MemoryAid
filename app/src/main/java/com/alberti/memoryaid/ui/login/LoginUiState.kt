package com.alberti.memoryaid.ui.login

data class LoginUiState(
    val mostrarDialogoPin: Boolean = false,
    val esPrimeraVezAdmin: Boolean = false,
    val pinInput: String = "",
    val errorPin: String? = null,
    val loginExitoso: Boolean = false
)
