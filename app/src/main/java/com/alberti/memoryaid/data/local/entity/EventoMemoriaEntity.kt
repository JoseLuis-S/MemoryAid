package com.alberti.memoryaid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eventos_memoria")
data class EventoMemoriaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val descripcion: String,
    val fechaHora: Long,
    val tipo: String,
    val recordatorioActivo: Boolean,
    val fechaRecordatorio: Long?,
    val frecuenciaHoras: Int
)