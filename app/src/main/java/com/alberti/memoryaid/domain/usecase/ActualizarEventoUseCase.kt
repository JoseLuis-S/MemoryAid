package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject

class ActualizarEventoUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    suspend operator fun invoke(
        id: Long,
        titulo: String,
        descripcion: String,
        fechaHora: Long,
        tipo: TipoEvento?,
        recordatorioActivo: Boolean,
        fechaRecordatorio: Long?,
        frecuenciaHoras: Int
    ): Result<Unit> {
        if (titulo.isBlank()) return Result.failure(Exception("El título es obligatorio"))

        val evento = EventoMemoria(
            id = id,
            titulo = titulo,
            descripcion = descripcion,
            fechaHora = fechaHora,
            tipo = tipo,
            recordatorioActivo = recordatorioActivo,
            fechaRecordatorio = fechaRecordatorio,
            frecuenciaHoras = frecuenciaHoras
        )

        return try {
            repositorio.actualizarEvento(evento)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
