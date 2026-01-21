package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject

class AgregarEventoUseCase @Inject constructor(private val repositorio: RepositorioMemoria) {

    suspend operator fun invoke(
        titulo: String,
        descripcion: String,
        fechaHora: Long,
        tipo: TipoEvento?,
        recordatorioActivo: Boolean,
        fechaRecordatorio: Long?,
        frecuenciaHoras: Int
    ): Result<Long> {

        if (titulo.isBlank()) {
            return Result.failure(Exception("El título no puede estar vacío"))
        }

        val nuevoEvento = EventoMemoria(
            titulo = titulo,
            descripcion = descripcion,
            fechaHora = fechaHora,
            tipo = tipo,
            recordatorioActivo = recordatorioActivo,
            fechaRecordatorio = fechaRecordatorio,
            frecuenciaHoras = frecuenciaHoras
        )

        return try {
            val id = repositorio.insertarEvento(nuevoEvento)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
