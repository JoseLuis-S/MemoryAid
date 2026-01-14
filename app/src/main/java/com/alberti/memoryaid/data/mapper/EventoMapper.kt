package com.alberti.memoryaid.data.mapper

import com.alberti.memoryaid.data.local.entity.EventoMemoriaEntity
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento

fun EventoMemoriaEntity.toDomain(): EventoMemoria {
    return EventoMemoria(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        fechaHora = fechaHora,
        tipo = TipoEvento.valueOf(tipo)
    )
}

fun EventoMemoria.toEntity(): EventoMemoriaEntity {
    return EventoMemoriaEntity(
        id = id ?: 0,
        titulo = titulo,
        descripcion = descripcion,
        fechaHora = fechaHora,
        tipo = tipo?.name ?: TipoEvento.NOTAS_GENERALES.name
    )
}