package com.alberti.memoryaid.data.mapper

import com.alberti.memoryaid.data.local.entity.EventoMemoriaEntity
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento

/**
 * Convierte una entidad de base de datos [EventoMemoriaEntity] en un modelo de dominio [EventoMemoria].
 * * Se utiliza para desacoplar la capa de datos de la capa de dominio, permitiendo que la lógica
 * de negocio trabaje con modelos puros.
 * * @throws IllegalArgumentException Si el valor de `tipo` en la base de datos no coincide con ningún [TipoEvento].
 * @return Un objeto de dominio con los datos mapeados.
 */
fun EventoMemoriaEntity.toDomain(): EventoMemoria {
    return EventoMemoria(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        fechaHora = fechaHora,
        tipo = TipoEvento.valueOf(tipo),
        recordatorioActivo = recordatorioActivo,
        fechaRecordatorio = fechaRecordatorio,
        frecuenciaHoras = frecuenciaHoras
    )
}

/**
 * Convierte un modelo de dominio [EventoMemoria] en una entidad de persistencia [EventoMemoriaEntity].
 * * Facilita la preparación de datos para operaciones de inserción o actualización en Room.
 * * @return Una instancia de [EventoMemoriaEntity]. Si el `id` del dominio es nulo,
 * se asigna 0 para que Room lo gestione como autogenerado.
 */
fun EventoMemoria.toEntity(): EventoMemoriaEntity {
    return EventoMemoriaEntity(
        id = id ?: 0,
        titulo = titulo,
        descripcion = descripcion,
        fechaHora = fechaHora,
        tipo = tipo?.name ?: TipoEvento.NOTAS_GENERALES.name,
        recordatorioActivo = recordatorioActivo,
        fechaRecordatorio = fechaRecordatorio,
        frecuenciaHoras = frecuenciaHoras
    )
}
