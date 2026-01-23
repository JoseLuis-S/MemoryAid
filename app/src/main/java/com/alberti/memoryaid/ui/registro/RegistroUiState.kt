package com.alberti.memoryaid.ui.registro

import com.alberti.memoryaid.domain.model.TipoEvento

/**
 * Representa el estado atómico y reactivo de la pantalla de Registro/Edición.
 * * Centraliza toda la información del formulario, estados de carga y visibilidad de
 * componentes de la interfaz. Al ser una [data class] inmutable, garantiza que cada
 * cambio genere una nueva versión del estado, facilitando la depuración y el testeo.
 *
 * @property id Identificador único del evento. Es `null` si se está creando un nuevo registro.
 * @property titulo Nombre o encabezado de la actividad.
 * @property descripcion Notas detalladas o información adicional del evento.
 * @property fechaHora Marca de tiempo (ms) que indica cuándo ocurre el evento.
 * @property tipo Categoría del evento (ej: Medicación, Crisis) definida en [TipoEvento].
 * @property recordatorioActivo Indica si el sistema debe programar una alarma para este evento.
 * @property fechaRecordatorio Timestamp (ms) programado para el primer aviso de recordatorio.
 * @property frecuenciaHoras Intervalo de repetición del recordatorio (0 para avisos únicos).
 * @property esEdicion Flag que determina si la pantalla carga datos existentes o un formulario vacío.
 * @property estaGuardando Estado de carga que bloquea la interacción mientras se persiste en DB.
 * @property registroExitoso Disparador para la navegación hacia atrás tras un guardado correcto.
 * @property mostrarAnimacionExito Controla la ejecución del feedback visual (Lottie) tras guardar.
 * @property error Mensaje de error de validación o fallo en la capa de datos.
 * @property mostrarDatePicker Controla la visibilidad del diálogo de selección de fecha.
 * @property mostrarTimePicker Controla la visibilidad del diálogo de selección de hora.
 * @property pinInput Buffer temporal para entradas de seguridad (si se requiere validación).
 */
data class RegistroUiState(
    val id: Long? = null,
    val titulo: String = "",
    val descripcion: String = "",
    val fechaHora: Long = System.currentTimeMillis(),
    val tipo: TipoEvento? = null,
    val recordatorioActivo: Boolean = false,
    val fechaRecordatorio: Long? = null,
    val frecuenciaHoras: Int = 0,
    val esEdicion: Boolean = false,
    val estaGuardando: Boolean = false,
    val registroExitoso: Boolean = false,
    val mostrarAnimacionExito: Boolean = false,
    val error: String? = null,
    val mostrarDatePicker: Boolean = false,
    val mostrarTimePicker: Boolean = false,
    val pinInput: String = ""
)
