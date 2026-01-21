package com.alberti.memoryaid.ui.registro

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alberti.memoryaid.data.worker.NotificacionScheduler
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import com.alberti.memoryaid.domain.usecase.AgregarEventoUseCase
import com.alberti.memoryaid.domain.usecase.ActualizarEventoUseCase
import com.alberti.memoryaid.navigation.Rutas.RutaRegistro
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val agregarEventoUseCase: AgregarEventoUseCase,
    private val actualizarEventoUseCase: ActualizarEventoUseCase,
    private val repositorio: RepositorioMemoria,
    @ApplicationContext private val context: Context,
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
                    recordatorioActivo = evento.recordatorioActivo,
                    fechaRecordatorio = evento.fechaRecordatorio,
                    frecuenciaHoras = evento.frecuenciaHoras,
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

    fun onRecordatorioToggled(activo: Boolean) {
        _state.update { it.copy(recordatorioActivo = activo) }
    }

    fun onFrecuenciaChanged(frecuencia: Int) {
        _state.update { it.copy(frecuenciaHoras = frecuencia) }
    }

    fun toggleDatePicker(mostrar: Boolean) {
        _state.update { it.copy(mostrarDatePicker = mostrar) }
    }

    fun toggleTimePicker(mostrar: Boolean) {
        _state.update { it.copy(mostrarTimePicker = mostrar) }
    }

    fun onDateSelected(millis: Long?) {
        millis?.let {
            val cal = Calendar.getInstance().apply { timeInMillis = it }
            val currentRecordatorio = Calendar.getInstance().apply {
                timeInMillis = _state.value.fechaRecordatorio ?: System.currentTimeMillis()
            }
            currentRecordatorio.set(Calendar.YEAR, cal.get(Calendar.YEAR))
            currentRecordatorio.set(Calendar.MONTH, cal.get(Calendar.MONTH))
            currentRecordatorio.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))

            _state.update { it.copy(fechaRecordatorio = currentRecordatorio.timeInMillis, mostrarDatePicker = false, mostrarTimePicker = true) }
        }
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        val cal = Calendar.getInstance().apply {
            timeInMillis = _state.value.fechaRecordatorio ?: System.currentTimeMillis()
        }
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)

        _state.update { it.copy(fechaRecordatorio = cal.timeInMillis, mostrarTimePicker = false) }
    }

    fun guardarEvento() {
        viewModelScope.launch {
            _state.update { it.copy(estaGuardando = true) }

            val st = _state.value
            val esEdicion = st.esEdicion
            val idExistente = st.id

            val resultado = if (esEdicion) {
                actualizarEventoUseCase(
                    id = idExistente ?: 0L,
                    titulo = st.titulo,
                    descripcion = st.descripcion,
                    fechaHora = st.fechaHora,
                    tipo = st.tipo,
                    recordatorioActivo = st.recordatorioActivo,
                    fechaRecordatorio = st.fechaRecordatorio,
                    frecuenciaHoras = st.frecuenciaHoras
                )
            } else {
                agregarEventoUseCase(
                    titulo = st.titulo,
                    descripcion = st.descripcion,
                    fechaHora = st.fechaHora,
                    tipo = st.tipo,
                    recordatorioActivo = st.recordatorioActivo,
                    fechaRecordatorio = st.fechaRecordatorio,
                    frecuenciaHoras = st.frecuenciaHoras
                )
            }

            resultado.onSuccess { idRetornado ->
                val idFinal = if (esEdicion) idExistente else (idRetornado as? Long)
                idFinal?.let { gestionarNotificacion(it) }

                _state.update { it.copy(estaGuardando = false, mostrarAnimacionExito = true) }
                delay(2000)
                _state.update { it.copy(registroExitoso = true) }
            }.onFailure { e ->
                _state.update { it.copy(estaGuardando = false, error = e.message) }
            }
        }
    }

    private fun gestionarNotificacion(id: Long) {
        val st = _state.value
        if (st.recordatorioActivo && st.fechaRecordatorio != null) {
            val delay = st.fechaRecordatorio - System.currentTimeMillis()
            if (delay > 0) {
                if (st.frecuenciaHoras > 0) {
                    NotificacionScheduler.programarRecordatorioPeriodico(
                        context, id, st.titulo, "Recordatorio cada ${st.frecuenciaHoras}h", st.frecuenciaHoras.toLong()
                    )
                } else {
                    NotificacionScheduler.programarRecordatorioUnico(
                        context, id, st.titulo, st.descripcion, delay
                    )
                }
            }
        } else {
            NotificacionScheduler.cancelarRecordatorio(context, id)
        }
    }
}
