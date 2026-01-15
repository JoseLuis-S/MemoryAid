package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import javax.inject.Inject

class PurgarDatosUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    suspend operator fun invoke() {
        repositorio.borrarTodo()
    }
}