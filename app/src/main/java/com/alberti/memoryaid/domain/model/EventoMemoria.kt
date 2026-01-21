package com.alberti.memoryaid.domain.model

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
