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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val obtenerEventosUseCase: ObtenerEventosUseCase,
    private val eliminarEventoUseCase: EliminarEventoUseCase,
    private val obtenerResumenDiarioUseCase: ObtenerResumenDiarioUseCase,
    private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()

    private val _filtro = MutableStateFlow<TipoEvento?>(null)

    private val _eventoABorrar = MutableStateFlow<EventoMemoria?>(null)
    val eventoABorrar = _eventoABorrar.asStateFlow()

    val role = sessionManager.rolActual

    val contactoEmergencia = sessionManager.contactoEmergencia.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val resumenDiario = obtenerResumenDiarioUseCase(fecha = System.currentTimeMillis())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val eventos = combine(_busqueda, _filtro) { query, filtro ->
        Pair(query, filtro)
    }.flatMapLatest { (query, filtro) ->
        obtenerEventosUseCase(filtro, query)
    }.onEach { lista ->
        _uiState.update { it.copy(eventos = lista, estaCargando = false) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun alCambiarBusqueda(nuevaBusqueda: String) {
        _busqueda.value = nuevaBusqueda
    }

    fun alCambiarFiltro(tipo: TipoEvento?) {
        _filtro.value = tipo
        _uiState.update { it.copy(filtroSeleccionado = tipo) }
    }

    fun guardarContacto(numero: String) {
        viewModelScope.launch {
            sessionManager.guardarContactoEmergencia(numero)
            _uiState.update { it.copy(mostrarDialogoConfigContacto = false) }
        }
    }

    fun mostrarConfigContacto(mostrar: Boolean) {
        _uiState.update { it.copy(mostrarDialogoConfigContacto = mostrar) }
    }

    fun realizarLlamadaDirecta(context: Context, numero: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$numero")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    fun mostrarConfirmacionBorrado(evento: EventoMemoria) {
        _eventoABorrar.value = evento
    }

    fun cancelarBorrado() {
        _eventoABorrar.value = null
    }

    fun confirmarBorrado() {
        viewModelScope.launch {
            _eventoABorrar.value?.let { evento ->
                NotificacionScheduler.cancelarRecordatorio(context, evento.id ?: 0L)
                eliminarEventoUseCase(evento)
                _eventoABorrar.value = null
            }
        }
    }

    fun alClickAdmin() {
        if (sessionManager.rolActual.value == UserRole.ADMIN) {
            _uiState.update { it.copy(navegarAAdmin = true) }
        } else {
            _uiState.update { it.copy(mostrarDialogoPin = true, pinInput = "", errorPin = null) }
        }
    }

    fun alCambiarPin(nuevo: String) {
        if (nuevo.length <= 4) {
            _uiState.update { it.copy(pinInput = nuevo, errorPin = null) }
        }
    }

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

    fun ocultarDialogoPin() {
        _uiState.update { it.copy(mostrarDialogoPin = false) }
    }

    fun resetNavegacionAdmin() {
        _uiState.update { it.copy(navegarAAdmin = false) }
    }
}
