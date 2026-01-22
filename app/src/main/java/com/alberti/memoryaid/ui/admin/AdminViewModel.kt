package com.alberti.memoryaid.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberti.memoryaid.data.local.SessionManager
import com.alberti.memoryaid.domain.usecase.GenerarInformeUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerEstadisticasUseCase
import com.alberti.memoryaid.domain.usecase.PurgarDatosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val obtenerEstadisticasUseCase: ObtenerEstadisticasUseCase,
    private val generarInformeUseCase: GenerarInformeUseCase,
    private val purgarDatosUseCase: PurgarDatosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState(estaCargando = true))
    val uiState = _uiState.asStateFlow()

    init {
        verificarConfiguracion()
    }

    private fun verificarConfiguracion() {
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            val pin = sessionManager.obtenerPin().firstOrNull()

            if (pin.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        necesitaConfiguracion = true,
                        mostrarDialogoPin = true,
                        estaCargando = false
                    )
                }
            } else {
                _uiState.update { it.copy(necesitaConfiguracion = false) }
                cargarEstadisticas()
            }
        }
    }

    private fun cargarEstadisticas() {
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            try {
                val stats = obtenerEstadisticasUseCase()
                _uiState.update { it.copy(estadisticas = stats, estaCargando = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(estaCargando = false) }
            }
        }
    }

    fun alCambiarNuevoPin(input: String) {
        if (input.length <= 4) {
            _uiState.update { it.copy(nuevoPinInput = input, errorValidacion = null) }
        }
    }

    fun confirmarCambioPin() {
        val pin = _uiState.value.nuevoPinInput
        if (pin.length < 4) {
            _uiState.update { it.copy(errorValidacion = "El PIN debe tener 4 dígitos") }
            return
        }

        viewModelScope.launch {
            sessionManager.guardarPin(pin)
            _uiState.update {
                it.copy(
                    mostrarDialogoPin = false,
                    nuevoPinInput = "",
                    necesitaConfiguracion = false
                )
            }
            cargarEstadisticas()
        }
    }

    fun alCambiarNuevoEmergencia(input: String) {
        _uiState.update { it.copy(nuevoEmergenciaInput = input) }
    }

    fun confirmarCambioEmergencia() {
        viewModelScope.launch {
            sessionManager.guardarContactoEmergencia(_uiState.value.nuevoEmergenciaInput)
            _uiState.update { it.copy(mostrarDialogoEmergencia = false, nuevoEmergenciaInput = "") }
        }
    }

    fun exportarInforme() {
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            val informe = generarInformeUseCase()
            _uiState.update { it.copy(informeGenerado = informe, estaCargando = false) }
        }
    }

    fun informeConsumido() {
        _uiState.update { it.copy(informeGenerado = null) }
    }

    fun purgarBaseDeDatos() {
        viewModelScope.launch {
            purgarDatosUseCase()
            _uiState.update { it.copy(mostrarConfirmacionPurga = false) }
            cargarEstadisticas()
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            sessionManager.logout()
        }
    }

    fun mostrarDialogoPin(mostrar: Boolean) {
        if (_uiState.value.necesitaConfiguracion && !mostrar) return
        _uiState.update { it.copy(mostrarDialogoPin = mostrar, errorValidacion = null) }
    }

    fun mostrarDialogoEmergencia(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarDialogoEmergencia = mostrar) }
    }

    fun mostrarDialogoPurga(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarConfirmacionPurga = mostrar) }
    }
}
