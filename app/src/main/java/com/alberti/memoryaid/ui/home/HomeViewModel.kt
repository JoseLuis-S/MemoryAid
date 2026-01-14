package com.alberti.memoryaid.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.usecase.EliminarEventoUseCase
import com.alberti.memoryaid.domain.usecase.FiltrarEventosUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerResumenDiarioUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerTimelineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val obtenerTimelineUseCase: ObtenerTimelineUseCase,
    private val filtrarEventosUseCase: FiltrarEventosUseCase,
    private val eliminarEventoUseCase: EliminarEventoUseCase,
    private val obtenerResumenDiarioUseCase: ObtenerResumenDiarioUseCase
) : ViewModel() {

    private val _filtro = MutableStateFlow<TipoEvento?>(null)
    private val _resumenDiario = MutableStateFlow<Map<TipoEvento, Int>>(emptyMap())
    val resumenDiario = _resumenDiario.asStateFlow()

    init {
        cargarResumenDelDia()
    }

    private fun cargarResumenDelDia() {
        viewModelScope.launch {
            obtenerResumenDiarioUseCase(System.currentTimeMillis()).collect { mapa ->
                _resumenDiario.value = mapa
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> = _filtro.flatMapLatest { filtro ->
        val flujoEventos = if (filtro == null) {
            obtenerTimelineUseCase()
        } else {
            filtrarEventosUseCase(filtro)
        }

        flujoEventos.map { lista ->
            HomeUiState(eventos = lista, filtroSeleccionado = filtro, estaCargando = false)
        }
    }
        .onStart {
            emit(HomeUiState(estaCargando = true))
        }
        .catch { e ->
            emit(HomeUiState(mensajeError = "Error al cargar datos: ${e.message}"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(estaCargando = true)
        )

    fun alCambiarFiltro(tipo: TipoEvento?) {
        _filtro.value = tipo
    }

    fun alEliminarEvento(evento: EventoMemoria) {
        viewModelScope.launch {
            eliminarEventoUseCase(evento)
        }
    }
}
