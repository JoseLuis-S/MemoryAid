package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject

/**
 * Caso de uso encargado de la actualización de un evento de memoria existente.
 * * Centraliza la lógica de validación previa a la persistencia, asegurando que los
 * datos del evento cumplan con las reglas de negocio mínimas (como el título no vacío)
 * antes de invocar al repositorio.
 * * @property repositorio Interfaz del repositorio para la gestión de datos.
 */
class ActualizarEventoUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    /**
     * Ejecuta la actualización de un evento.
     * * @param id Identificador único del evento a actualizar.
     * @param titulo Nuevo título del evento.
     * @param descripcion Nueva descripción o detalles.
     * @param fechaHora Marca de tiempo de la ocurrencia del evento.
     * @param tipo Categoría del evento representada por [TipoEvento].
     * @param recordatorioActivo Estado del recordatorio.
     * @param fechaRecordatorio Marca de tiempo para la notificación (opcional).
     * @param frecuenciaHoras Intervalo de repetición del recordatorio.
     * * @return [Result] con [Unit] en caso de éxito, o una excepción si el título
     * es inválido o la operación de persistencia falla.
     */
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
        // Validación de regla de negocio
        if (titulo.isBlank()) {
            return Result.failure(Exception("El título es obligatorio"))
        }

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
