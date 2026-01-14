package com.alberti.memoryaid.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.usecase.EliminarEventoUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerEventosUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerResumenDiarioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eliminarEventoUseCase: EliminarEventoUseCase,
    private val obtenerResumenDiarioUseCase: ObtenerResumenDiarioUseCase,
    private val obtenerEventosUseCase: ObtenerEventosUseCase
) : ViewModel() {

    private val _filtro = MutableStateFlow<TipoEvento?>(null)
    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()
    private val _eventoABorrar = MutableStateFlow<EventoMemoria?>(null)
    val eventoABorrar = _eventoABorrar.asStateFlow()

    private val _resumenDiario = MutableStateFlow<Map<TipoEvento, Int>>(emptyMap())
    val resumenDiario = _resumenDiario.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> = combine(_filtro, _busqueda) { filtro, query ->
        filtro to query
    }.flatMapLatest { (filtro, query) ->
        obtenerEventosUseCase(filtro, query)
            .map { lista ->
                HomeUiState(
                    eventos = lista,
                    filtroSeleccionado = filtro,
                    estaCargando = false
                )
            }
    }
        .onStart { emit(HomeUiState(estaCargando = true)) }
        .catch { e -> emit(HomeUiState(mensajeError = "Error: ${e.message}")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(estaCargando = true)
        )

    init {
        cargarResumenDelDia()
    }

    fun alCambiarBusqueda(nuevaQuery: String) {
        _busqueda.value = nuevaQuery
    }

    fun alCambiarFiltro(tipo: TipoEvento?) {
        _filtro.value = tipo
    }

    fun alEliminarEvento(evento: EventoMemoria) {
        viewModelScope.launch { eliminarEventoUseCase(evento) }
    }

    private fun cargarResumenDelDia() {
        viewModelScope.launch {
            obtenerResumenDiarioUseCase(System.currentTimeMillis()).collect { mapa ->
                _resumenDiario.value = mapa
            }
        }
    }

    fun mostrarConfirmacionBorrado(evento: EventoMemoria) {
        _eventoABorrar.value = evento
    }

    fun cancelarBorrado() {
        _eventoABorrar.value = null
    }

    fun confirmarBorrado() {
        _eventoABorrar.value?.let { evento ->
            viewModelScope.launch {
                eliminarEventoUseCase(evento)
                _eventoABorrar.value = null
            }
        }
    }
}
