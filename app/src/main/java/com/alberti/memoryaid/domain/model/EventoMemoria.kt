package com.alberti.memoryaid.domain.model

/**
 * Representa un único evento o registro de memoria dentro de la aplicación.
 *
 * Esta clase de datos contiene toda la información relevante sobre un suceso específico
 * relacionado con el cuidado del paciente, como la toma de medicación, una crisis, etc.
 *
 * @property id El identificador único del evento en la base de datos. Es nulo si el evento aún no ha sido guardado.
 * @property titulo Un título breve y descriptivo para el evento.
 * @property descripcion Una descripción más detallada de lo que ocurrió durante el evento.
 * @property fechaHora La marca de tiempo (timestamp en milisegundos) que indica cuándo ocurrió exactamente el evento.
 * @property tipo La categoría del evento, definida por la enumeración [TipoEvento]. Puede ser nulo si no se ha asignado una categoría.
 * @property recordatorioActivo Un indicador booleano (`true`/`false`) para saber si hay una notificación de recordatorio activa para este evento.
 * @property fechaRecordatorio La marca de tiempo específica para el próximo recordatorio. Es nulo si no hay ningún recordatorio programado.
 * @property frecuenciaHoras La frecuencia en horas con la que se debe repetir un recordatorio. Un valor de `0` indica que el recordatorio no es recurrente.
 */
data class EventoMemoria(
    val id: Long? = null,
    val titulo: String,
    val descripcion: String,
    val fechaHora: Long,
    val tipo: TipoEvento? = null,
    val recordatorioActivo: Boolean = false,
    val fechaRecordatorio: Long? = null,
    val frecuenciaHoras: Int = 0
)
