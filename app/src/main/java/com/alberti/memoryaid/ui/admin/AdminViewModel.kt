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

/**
 * ViewModel encargado de la lógica de negocio para la pantalla de Administración.
 * * Gestiona el flujo de datos unidireccional (UDF) para el panel de control,
 * encargándose de la seguridad (PIN), métricas, exportación de informes
 * y mantenimiento de la base de datos.
 *
 * @property sessionManager Gestor de preferencias y sesión del usuario.
 * @property obtenerEstadisticasUseCase Interactor para el cálculo de métricas semanales.
 * @property generarInformeUseCase Interactor para la creación del reporte en texto plano.
 * @property purgarDatosUseCase Interactor para la eliminación masiva de registros.
 */
@HiltViewModel
class AdminViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val obtenerEstadisticasUseCase: ObtenerEstadisticasUseCase,
    private val generarInformeUseCase: GenerarInformeUseCase,
    private val purgarDatosUseCase: PurgarDatosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState(estaCargando = true))

    /**
     * Estado de la interfaz de usuario expuesto como un flujo inmutable.
     */
    val uiState = _uiState.asStateFlow()

    init {
        verificarConfiguracion()
    }

    /**
     * Comprueba si existe un PIN configurado en el sistema.
     * * Si no existe, fuerza el estado de configuración inicial para asegurar
     * el acceso restringido.
     */
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

    /**
     * Recupera las métricas de uso y tendencias desde la capa de dominio.
     */
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

    /**
     * Actualiza el buffer temporal del nuevo PIN y limpia errores de validación.
     */
    fun alCambiarNuevoPin(input: String) {
        if (input.length <= 4) {
            _uiState.update { it.copy(nuevoPinInput = input, errorValidacion = null) }
        }
    }

    /**
     * Persiste el nuevo PIN en el almacenamiento seguro.
     * * Valida que cumpla con la longitud mínima antes de proceder.
     */
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

    /**
     * Actualiza el buffer temporal para el número de emergencia.
     */
    fun alCambiarNuevoEmergencia(input: String) {
        _uiState.update { it.copy(nuevoEmergenciaInput = input) }
    }

    /**
     * Persiste el contacto de emergencia y cierra el diálogo.
     */
    fun confirmarCambioEmergencia() {
        viewModelScope.launch {
            sessionManager.guardarContactoEmergencia(_uiState.value.nuevoEmergenciaInput)
            _uiState.update { it.copy(mostrarDialogoEmergencia = false, nuevoEmergenciaInput = "") }
        }
    }

    /**
     * Dispara la generación del informe clínico.
     * * El resultado se inyecta en el estado para que la UI lo procese mediante un Intent.
     */
    fun exportarInforme() {
        viewModelScope.launch {
            _uiState.update { it.copy(estaCargando = true) }
            val informe = generarInformeUseCase()
            _uiState.update { it.copy(informeGenerado = informe, estaCargando = false) }
        }
    }

    /**
     * Notifica al ViewModel que la UI ya procesó el informe generado, evitando
     * que se disparen eventos duplicados en recomposiciones.
     */
    fun informeConsumido() {
        _uiState.update { it.copy(informeGenerado = null) }
    }

    /**
     * Ejecuta el purgado total de los datos de la aplicación y refresca el estado.
     */
    fun purgarBaseDeDatos() {
        viewModelScope.launch {
            purgarDatosUseCase()
            _uiState.update { it.copy(mostrarConfirmacionPurga = false) }
            cargarEstadisticas()
        }
    }

    /**
     * Elimina los datos de sesión activa para retornar al estado de login.
     */
    fun cerrarSesion() {
        viewModelScope.launch {
            sessionManager.logout()
        }
    }

    // --- Control de visibilidad de componentes de UI ---

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
