package com.alberti.memoryaid.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberti.memoryaid.data.local.SessionManager
import com.alberti.memoryaid.domain.usecase.GenerarInformeUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerEstadisticasUseCase
import com.alberti.memoryaid.domain.usecase.PurgarDatosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val obtenerEstadisticasUseCase: ObtenerEstadisticasUseCase,
    private val purgarDatosUseCase: PurgarDatosUseCase,
    private val generarInformeUseCase: GenerarInformeUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState = _uiState.asStateFlow()

    val contactoActual = sessionManager.contactoEmergencia.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    init {
        cargarEstadisticas()
    }

    private fun cargarEstadisticas() {
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            try {
                val stats = obtenerEstadisticasUseCase()
                _uiState.update { it.copy(estadisticas = stats, estaCargando = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(estaCargando = false, mensajeError = "Error al cargar datos") }
            }
        }
    }

    fun mostrarDialogoPin(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarDialogoPin = mostrar, nuevoPinInput = "", errorValidacion = null) }
    }

    fun alCambiarNuevoPin(input: String) {
        if (input.length <= 4) {
            _uiState.update { it.copy(nuevoPinInput = input, errorValidacion = null) }
        }
    }

    fun confirmarCambioPin() {
        if (_uiState.value.nuevoPinInput.length == 4) {
            viewModelScope.launch {
                sessionManager.guardarPin(_uiState.value.nuevoPinInput)
                _uiState.update { it.copy(mostrarDialogoPin = false) }
            }
        } else {
            _uiState.update { it.copy(errorValidacion = "El PIN debe tener 4 dígitos") }
        }
    }

    fun mostrarDialogoEmergencia(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarDialogoEmergencia = mostrar, nuevoEmergenciaInput = contactoActual.value ?: "", errorValidacion = null) }
    }

    fun alCambiarNuevoEmergencia(input: String) {
        _uiState.update { it.copy(nuevoEmergenciaInput = input) }
    }

    fun confirmarCambioEmergencia() {
        viewModelScope.launch {
            sessionManager.guardarContactoEmergencia(_uiState.value.nuevoEmergenciaInput)
            _uiState.update { it.copy(mostrarDialogoEmergencia = false) }
        }
    }

    fun purgarBaseDeDatos() {
        viewModelScope.launch {
            purgarDatosUseCase()
            cargarEstadisticas()
            _uiState.update { it.copy(mostrarConfirmacionPurga = false) }
        }
    }

    fun mostrarDialogoPurga(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarConfirmacionPurga = mostrar) }
    }

    fun exportarInforme() {
        viewModelScope.launch {
            try {
                val informe = generarInformeUseCase()
                _uiState.update { it.copy(informeGenerado = informe) }
            } catch (e: Exception) {
                _uiState.update { it.copy(mensajeError = "Fallo al exportar") }
            }
        }
    }

    fun informeConsumido() {
        _uiState.update { it.copy(informeGenerado = null) }
    }

    fun cerrarSesion() {
        sessionManager.logout()
    }
}
