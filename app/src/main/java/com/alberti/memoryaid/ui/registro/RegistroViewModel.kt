package com.alberti.memoryaid.ui.registro

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import com.alberti.memoryaid.domain.usecase.AgregarEventoUseCase
import com.alberti.memoryaid.domain.usecase.ActualizarEventoUseCase
import com.alberti.memoryaid.navigation.Rutas.RutaRegistro
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val agregarEventoUseCase: AgregarEventoUseCase,
    private val actualizarEventoUseCase: ActualizarEventoUseCase,
    private val repositorio: RepositorioMemoria,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(RegistroUiState())
    val state = _state.asStateFlow()

    private val argumentos = savedStateHandle.toRoute<RutaRegistro>()

    init {
        argumentos.eventoId?.let { id ->
            cargarEventoParaEditar(id)
        }
    }

    private fun cargarEventoParaEditar(id: Long) {
        viewModelScope.launch {
            repositorio.obtenerEventoPorId(id)?.let { evento ->
                _state.update { it.copy(
                    id = evento.id,
                    titulo = evento.titulo,
                    descripcion = evento.descripcion,
                    fechaHora = evento.fechaHora,
                    tipo = evento.tipo,
                    esEdicion = true
                )}
            }
        }
    }

    fun onTituloChanged(nuevoTitulo: String) {
        _state.update { it.copy(titulo = nuevoTitulo, error = null) }
    }

    fun onDescripcionChanged(nuevaDesc: String) {
        _state.update { it.copy(descripcion = nuevaDesc) }
    }

    fun onTipoChanged(nuevoTipo: TipoEvento?) {
        _state.update { it.copy(tipo = nuevoTipo) }
    }

    fun onFechaHoraChanged(nuevaFecha: Long) {
        _state.update { it.copy(fechaHora = nuevaFecha) }
    }

    fun guardarEvento() {
        viewModelScope.launch {
            _state.update { it.copy(estaGuardando = true) }

            val resultado = if (_state.value.esEdicion) {
                actualizarEventoUseCase(
                    id = _state.value.id ?: 0L,
                    titulo = _state.value.titulo,
                    descripcion = _state.value.descripcion,
                    fechaHora = _state.value.fechaHora,
                    tipo = _state.value.tipo
                )
            } else {
                agregarEventoUseCase(
                    titulo = _state.value.titulo,
                    descripcion = _state.value.descripcion,
                    fechaHora = _state.value.fechaHora,
                    tipo = _state.value.tipo
                )
            }

            resultado.onSuccess {
                _state.update { it.copy(estaGuardando = false, registroExitoso = true) }
            }.onFailure { e ->
                _state.update { it.copy(estaGuardando = false, error = e.message) }
            }
        }
    }
}
