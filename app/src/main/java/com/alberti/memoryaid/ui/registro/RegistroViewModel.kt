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

/**
 * ViewModel encargado de la lógica de creación y edición de eventos de memoria.
 * * Este componente orquestra la validación de entrada, la persistencia mediante casos de uso
 * y la programación de notificaciones del sistema. Utiliza [SavedStateHandle] para recuperar
 * argumentos de navegación de forma segura (Type-Safe).
 * * @property agregarEventoUseCase Interactor para registrar nuevos eventos.
 * @property actualizarEventoUseCase Interactor para modificar eventos existentes.
 * @property repositorio Abstracción de datos para la recuperación de eventos por ID.
 * @property context Contexto de aplicación para la gestión de recordatorios.
 * @property savedStateHandle Almacén de estado persistente que contiene los argumentos de ruta.
 */
@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val agregarEventoUseCase: AgregarEventoUseCase,
    private val actualizarEventoUseCase: ActualizarEventoUseCase,
    private val repositorio: RepositorioMemoria,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(RegistroUiState())

    /** Estado de la interfaz de usuario expuesto de forma inmutable. */
    val state = _state.asStateFlow()

    /** Argumentos de navegación recuperados mediante la clase serializable [RutaRegistro]. */
    private val argumentos = savedStateHandle.toRoute<RutaRegistro>()

    init {
        // Si recibimos un ID, activamos el modo edición cargando los datos previos.
        argumentos.eventoId?.let { id ->
            cargarEventoParaEditar(id)
        }
    }

    /** Recupera un evento de la base de datos y mapea sus propiedades al estado de la UI. */
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

    // --- Gestión de Entradas de Usuario ---

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

    // --- Control de Diálogos (Date & Time Pickers) ---

    fun toggleDatePicker(mostrar: Boolean) {
        _state.update { it.copy(mostrarDatePicker = mostrar) }
    }

    fun toggleTimePicker(mostrar: Boolean) {
        _state.update { it.copy(mostrarTimePicker = mostrar) }
    }

    /**
     * Procesa la fecha seleccionada y dispara automáticamente el selector de hora.
     * @param millis Marca de tiempo en milisegundos seleccionada en el DatePicker.
     */
    fun onDateSelected(millis: Long?) {
        millis?.let {
            val cal = Calendar.getInstance().apply { timeInMillis = it }
            val currentRecordatorio = Calendar.getInstance().apply {
                timeInMillis = _state.value.fechaRecordatorio ?: System.currentTimeMillis()
            }
            currentRecordatorio.set(Calendar.YEAR, cal.get(Calendar.YEAR))
            currentRecordatorio.set(Calendar.MONTH, cal.get(Calendar.MONTH))
            currentRecordatorio.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))

            _state.update { it.copy(
                fechaRecordatorio = currentRecordatorio.timeInMillis,
                mostrarDatePicker = false,
                mostrarTimePicker = true
            ) }
        }
    }

    /** Completa la configuración del recordatorio con la hora seleccionada. */
    fun onTimeSelected(hour: Int, minute: Int) {
        val cal = Calendar.getInstance().apply {
            timeInMillis = _state.value.fechaRecordatorio ?: System.currentTimeMillis()
        }
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)

        _state.update { it.copy(fechaRecordatorio = cal.timeInMillis, mostrarTimePicker = false) }
    }

    /**
     * Orquestador del guardado de datos.
     * Decide entre creación o actualización, gestiona la animación de éxito y
     * dispara la programación de notificaciones.
     */
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
                // Mantenemos el ID original si es edición, o usamos el nuevo ID generado.
                val idFinal = if (esEdicion) idExistente else (idRetornado as? Long)
                idFinal?.let { gestionarNotificacion(it) }

                _state.update { it.copy(estaGuardando = false, mostrarAnimacionExito = true) }
                delay(2000) // Tiempo para visualizar la animación de éxito
                _state.update { it.copy(registroExitoso = true) }
            }.onFailure { e ->
                _state.update { it.copy(estaGuardando = false, error = e.message) }
            }
        }
    }

    /**
     * Sincroniza los recordatorios de la aplicación con el estado del evento.
     * Programa avisos únicos o periódicos, o los cancela si el recordatorio fue desactivado.
     */
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
