package com.alberti.memoryaid.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberti.memoryaid.data.local.SessionManager
import com.alberti.memoryaid.domain.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun entrarComoUsuario() {
        sessionManager.setRole(UserRole.USER)
        _uiState.update { it.copy(loginExitoso = true) }
    }

    fun mostrarDialogoPin() {
        viewModelScope.launch {
            val pinExistente = sessionManager.obtenerPin().first()
            _uiState.update { it.copy(
                mostrarDialogoPin = true,
                esPrimeraVezAdmin = pinExistente == null,
                errorPin = null,
                pinInput = ""
            ) }
        }
    }

    fun ocultarDialogoPin() {
        _uiState.update { it.copy(mostrarDialogoPin = false) }
    }

    fun alCambiarPin(nuevoPin: String) {
        if (nuevoPin.length <= 4) {
            _uiState.update { it.copy(pinInput = nuevoPin, errorPin = null) }
        }
    }

    fun ejecutarAccionAdmin() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.esPrimeraVezAdmin) {
                if (state.pinInput.length == 4) {
                    sessionManager.guardarPin(state.pinInput)
                    _uiState.update { it.copy(loginExitoso = true, mostrarDialogoPin = false) }
                } else {
                    _uiState.update { it.copy(errorPin = "El PIN debe tener 4 dígitos") }
                }
            } else {
                val esValido = sessionManager.loginComoAdmin(state.pinInput)
                if (esValido) {
                    _uiState.update { it.copy(loginExitoso = true, mostrarDialogoPin = false) }
                } else {
                    _uiState.update { it.copy(errorPin = "PIN incorrecto") }
                }
            }
        }
    }
}