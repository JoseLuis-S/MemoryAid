package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject

/**
 * Caso de uso encargado de la eliminación de un evento de memoria.
 *
 * Actúa como un interactor que abstrae la lógica de borrado de la capa de datos.
 * Al residir en la capa de dominio, permite que la interfaz de usuario solicite
 * la eliminación de un registro sin conocer los detalles de implementación de la persistencia.
 *
 * @property repositorio Interfaz del repositorio de memoria para ejecutar la operación.
 */
class EliminarEventoUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    /**
     * Ejecuta la eliminación del evento proporcionado.
     *
     * @param evento El objeto de dominio [EventoMemoria] que se desea remover de la persistencia.
     */
    suspend operator fun invoke(evento: EventoMemoria) {
        repositorio.eliminarEvento(evento)
    }
}
