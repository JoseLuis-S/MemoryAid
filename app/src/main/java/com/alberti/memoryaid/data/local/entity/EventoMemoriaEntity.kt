package com.alberti.memoryaid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa la persistencia de un evento de memoria en la base de datos local.
 * Esta entidad define la estructura de la tabla `eventos_memoria`.
 *
 * @property id Identificador único autogenerado por Room para cada registro.
 * @property titulo Nombre descriptivo o encabezado del evento.
 * @property descripcion Detalle extendido o información adicional del evento.
 * @property fechaHora Marca de tiempo (Unix timestamp) en milisegundos que representa cuándo ocurre el evento.
 * @property tipo Categoría o etiqueta del evento (ej: "Salud", "Citas", "Social").
 * @property recordatorioActivo Indica si el sistema debe disparar notificaciones para este evento.
 * @property fechaRecordatorio Marca de tiempo opcional (Unix timestamp) para la notificación. Puede ser nulo si no hay recordatorio programado.
 * @property frecuenciaHoras Intervalo de repetición en horas. Un valor de 0 suele indicar que no se repite.
 */
@Entity(tableName = "eventos_memoria")
data class EventoMemoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val titulo: String,

    val descripcion: String,

    val fechaHora: Long,

    val tipo: String,

    val recordatorioActivo: Boolean,

    val fechaRecordatorio: Long?,

    val frecuenciaHoras: Int
)
