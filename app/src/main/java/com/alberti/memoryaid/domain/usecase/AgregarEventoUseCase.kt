package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject

/**
 * Caso de uso para la creación y registro de nuevos eventos de memoria.
 * * Actúa como un interactor que encapsula la lógica de negocio necesaria antes de
 * persistir un evento, asegurando la integridad de los datos mínimos requeridos.
 * * @property repositorio Abstracción del repositorio para operaciones de persistencia.
 */
class AgregarEventoUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {

    /**
     * Ejecuta la lógica para agregar un nuevo evento.
     * * Valida que el título cumpla con las reglas de negocio y delega la inserción
     * al repositorio. El ID del objeto [EventoMemoria] se omite en la construcción
     * inicial ya que la base de datos lo genera automáticamente.
     * * @param titulo Nombre identificador del evento. No debe estar vacío o compuesto solo por espacios.
     * @param descripcion Detalle informativo sobre el evento.
     * @param fechaHora Timestamp (ms) de cuándo ocurre el evento.
     * @param tipo Categoría del evento definida por [TipoEvento].
     * @param recordatorioActivo Flag para habilitar notificaciones.
     * @param fechaRecordatorio Timestamp (ms) programado para la alerta (opcional).
     * @param frecuenciaHoras Intervalo de repetición para eventos periódicos.
     * * @return [Result] que contiene el ID generado (Long) en caso de éxito,
     * o una excepción en caso de error de validación o fallo en la persistencia.
     */
    suspend operator fun invoke(
        titulo: String,
        descripcion: String,
        fechaHora: Long,
        tipo: TipoEvento?,
        recordatorioActivo: Boolean,
        fechaRecordatorio: Long?,
        frecuenciaHoras: Int
    ): Result<Long> {

        // Regla de Negocio: Validación de consistencia
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
