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
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        cargarEstadisticas()
    }

    private fun cargarEstadisticas() {
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            try {
                val stats = obtenerEstadisticasUseCase()
                _uiState.update { it.copy(
                    estadisticas = stats,
                    estaCargando = false,
                    mensajeError = null
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    estaCargando = false,
                    mensajeError = "Error al cargar datos"
                )}
            }
        }
    }

    fun cerrarSesion() {
        sessionManager.logout()
    }

    fun purgarBaseDeDatos() {
        viewModelScope.launch {
            purgarDatosUseCase()
            cargarEstadisticas()
        }
    }

    fun mostrarDialogoPurga(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarConfirmacionPurga = mostrar) }
    }

    fun exportarInforme() {
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            try {
                val textoInforme = generarInformeUseCase()
                _uiState.update { it.copy(informeGenerado = textoInforme, estaCargando = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(estaCargando = false) }
            }
        }
    }

    fun informeConsumido() {
        _uiState.update { it.copy(informeGenerado = null) }
    }
}
