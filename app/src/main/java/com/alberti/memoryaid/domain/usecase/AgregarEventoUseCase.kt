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
        tipo: TipoEvento?
    ): Result<Unit> {

        if (titulo.isBlank()) {
            return Result.failure(Exception("El título no puede estar vacío"))
        }

        val nuevoEvento = EventoMemoria(
            titulo = titulo,
            descripcion = descripcion,
            fechaHora = fechaHora,
            tipo = tipo
        )

        return try {
            repositorio.guardarEvento(nuevoEvento)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}