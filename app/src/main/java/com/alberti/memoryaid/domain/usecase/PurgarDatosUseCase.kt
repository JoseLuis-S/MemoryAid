package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import javax.inject.Inject

/**
 * Caso de uso encargado de la limpieza total de la persistencia de la aplicación.
 * * Este interactor ejecuta la "opción nuclear": elimina todos los registros de eventos
 * de memoria sin posibilidad de recuperación. Debe ser invocado con precaución,
 * generalmente desde una sección de configuración avanzada o mantenimiento.
 *
 * @property repositorio Interfaz del repositorio que gestiona la fuente de datos.
 */
class PurgarDatosUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    /**
     * Ejecuta la eliminación masiva de todos los eventos almacenados.
     * * Esta operación es destructiva y afecta a la tabla completa de eventos.
     */
    suspend operator fun invoke() {
        repositorio.borrarTodo()
    }
}
