package com.alberti.memoryaid.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberti.memoryaid.data.local.SessionManager
import com.alberti.memoryaid.data.worker.NotificacionScheduler
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.model.UserRole
import com.alberti.memoryaid.domain.usecase.EliminarEventoUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerEventosUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerResumenDiarioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel que orquestra la lógica de la pantalla de inicio (Home).
 * * Gestiona la búsqueda reactiva, el filtrado por categorías, la gestión de sesiones
 * de administrador y las acciones de emergencia.
 * * Utiliza un patrón de **Unidirectional Data Flow (UDF)** para exponer el estado
 * a la UI de forma consistente.
 * * @property obtenerEventosUseCase Caso de uso para recuperar eventos filtrados.
 * @property eliminarEventoUseCase Caso de uso para la remoción de registros.
 * @property obtenerResumenDiarioUseCase Caso de uso para obtener métricas del día actual.
 * @property sessionManager Gestor de persistencia de preferencias y roles.
 * @property context Contexto de la aplicación inyectado para operaciones de sistema (Llamadas, Alarmas).
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val obtenerEventosUseCase: ObtenerEventosUseCase,
    private val eliminarEventoUseCase: EliminarEventoUseCase,
    private val obtenerResumenDiarioUseCase: ObtenerResumenDiarioUseCase,
    private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    /** Estado principal observado por la UI para renderizar componentes informativos y diálogos. */
    val uiState = _uiState.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    /** Flujo que emite el texto actual de la barra de búsqueda. */
    val busqueda = _busqueda.asStateFlow()

    private val _filtro = MutableStateFlow<TipoEvento?>(null)

    private val _eventoABorrar = MutableStateFlow<EventoMemoria?>(null)
    /** Expone el evento seleccionado para eliminación, permitiendo mostrar el diálogo de confirmación. */
    val eventoABorrar = _eventoABorrar.asStateFlow()

    /** Rol actual del usuario (USER/ADMIN) observado desde el DataStore. */
    val role = sessionManager.rolActual

    /** Flujo con el número de contacto de emergencia guardado. */
    val contactoEmergencia = sessionManager.contactoEmergencia.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    /** * Resumen de actividad del día actual.
     * Se actualiza automáticamente si los datos subyacentes cambian.
     */
    val resumenDiario = obtenerResumenDiarioUseCase(fecha = System.currentTimeMillis())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    /**
     * Flujo principal de eventos.
     * Combina la búsqueda de texto y el filtro de categoría para disparar una nueva
     * consulta cada vez que uno de los dos parámetros cambie.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val eventos = combine(_busqueda, _filtro) { query, filtro ->
        Pair(query, filtro)
    }.flatMapLatest { (query, filtro) ->
        obtenerEventosUseCase(filtro, query)
    }.onEach { lista ->
        _uiState.update { it.copy(eventos = lista, estaCargando = false) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Actualiza el estado de búsqueda. */
    fun alCambiarBusqueda(nuevaBusqueda: String) {
        _busqueda.value = nuevaBusqueda
    }

    /** Actualiza el filtro por categoría y notifica al estado de la UI. */
    fun alCambiarFiltro(tipo: TipoEvento?) {
        _filtro.value = tipo
        _uiState.update { it.copy(filtroSeleccionado = tipo) }
    }

    /** Persiste el número de emergencia en el almacenamiento local. */
    fun guardarContacto(numero: String) {
        viewModelScope.launch {
            sessionManager.guardarContactoEmergencia(numero)
            _uiState.update { it.copy(mostrarDialogoConfigContacto = false) }
        }
    }

    /** Controla la visibilidad del diálogo de configuración de contacto. */
    fun mostrarConfigContacto(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarDialogoConfigContacto = mostrar) }
    }

    /** * Inicia una actividad de llamada telefónica.
     * Requiere que el permiso CALL_PHONE haya sido gestionado previamente en la UI.
     */
    fun realizarLlamadaDirecta(context: Context, numero: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$numero")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    /** Prepara el evento para ser borrado mostrando la confirmación en pantalla. */
    fun mostrarConfirmacionBorrado(evento: EventoMemoria) {
        _eventoABorrar.value = evento
    }

    /** Cancela el proceso de borrado actual. */
    fun cancelarBorrado() {
        _eventoABorrar.value = null
    }

    /** * Ejecuta la eliminación definitiva del evento.
     * Cancela cualquier recordatorio activo asociado antes de borrarlo de la base de datos.
     */
    fun confirmarBorrado() {
        viewModelScope.launch {
            _eventoABorrar.value?.let { evento ->
                NotificacionScheduler.cancelarRecordatorio(context, evento.id ?: 0L)
                eliminarEventoUseCase(evento)
                _eventoABorrar.value = null
            }
        }
    }

    /** * Gestiona el acceso al panel de administración.
     * Si no hay PIN configurado, fuerza el flujo de configuración inicial.
     */
    fun alClickAdmin() {
        viewModelScope.launch {
            if (sessionManager.rolActual.value == UserRole.ADMIN) {
                _uiState.update { it.copy(navegarAAdmin = true) }
            } else {
                val pinExistente = sessionManager.obtenerPin().firstOrNull()
                if (pinExistente.isNullOrBlank()) {
                    _uiState.update { it.copy(mostrarDialogoConfigInicial = true) }
                } else {
                    _uiState.update { it.copy(mostrarDialogoPin = true, pinInput = "", errorPin = null) }
                }
            }
        }
    }

    /** Actualiza el buffer del PIN introducido. */
    fun alCambiarPin(nuevo: String) {
        if (nuevo.length <= 4) {
            _uiState.update { it.copy(pinInput = nuevo, errorPin = null) }
        }
    }

    /** Valida el PIN contra el almacenamiento seguro y otorga acceso si es correcto. */
    fun validarPinAdmin() {
        viewModelScope.launch {
            val esValido = sessionManager.loginComoAdmin(_uiState.value.pinInput)
            if (esValido) {
                _uiState.update { it.copy(mostrarDialogoPin = false, navegarAAdmin = true) }
            } else {
                _uiState.update { it.copy(errorPin = "PIN incorrecto") }
            }
        }
    }

    /** Cierra el diálogo de entrada de PIN. */
    fun ocultarDialogoPin() {
        _uiState.update { it.copy(mostrarDialogoPin = false) }
    }

    /** Cierra el aviso de configuración inicial. */
    fun ocultarDialogoConfigInicial() {
        _uiState.update { it.copy(mostrarDialogoConfigInicial = false) }
    }

    /** Resetea el flag de navegación para evitar saltos repetidos en la UI. */
    fun resetNavegacionAdmin() {
        _uiState.update { it.copy(navegarAAdmin = false) }
    }
}
