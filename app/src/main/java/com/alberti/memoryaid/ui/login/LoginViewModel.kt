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

/**
 * ViewModel encargado de gestionar la lógica de autenticación y roles de acceso.
 *
 * Actúa como el puente entre la interfaz de usuario y el gestor de sesiones,
 * permitiendo el acceso rápido como cuidador (USER) o el acceso restringido mediante
 * PIN para el administrador (ADMIN).
 *
 * @property sessionManager Fuente de verdad para la persistencia de roles y credenciales.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())

    /**
     * Estado observable que representa la situación actual de la pantalla de login.
     */
    val uiState = _uiState.asStateFlow()

    /**
     * Establece el rol de usuario estándar e inicia la navegación al panel principal.
     */
    fun entrarComoUsuario() {
        sessionManager.setRole(UserRole.USER)
        _uiState.update { it.copy(loginExitoso = true) }
    }

    /**
     * Prepara y muestra el desafío de seguridad para el acceso administrativo.
     * * Consulta el almacenamiento para determinar si es la primera vez que se accede
     * (requiriendo configuración de PIN) o si se debe validar una credencial existente.
     */
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

    /**
     * Cierra el diálogo de seguridad y limpia los estados temporales.
     */
    fun ocultarDialogoPin() {
        _uiState.update { it.copy(mostrarDialogoPin = false) }
    }

    /**
     * Gestiona la entrada de texto del PIN, limitando la longitud a 4 dígitos.
     * * @param nuevoPin Cadena de caracteres introducida por el usuario.
     */
    fun alCambiarPin(nuevoPin: String) {
        if (nuevoPin.length <= 4) {
            _uiState.update { it.copy(pinInput = nuevoPin, errorPin = null) }
        }
    }

    /**
     * Orquestador de la acción administrativa según el contexto del estado.
     * * * **Modo Configuración:** Si es la primera vez, persiste el nuevo PIN.
     * * **Modo Validación:** Compara el PIN introducido con el almacenado.
     * * En ambos casos exitosos, actualiza el rol a ADMIN y dispara la navegación.
     */
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
