package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class PurgarDatosUseCaseTest {

    private val repositorio: RepositorioMemoria = mock()
    private val useCase = PurgarDatosUseCase(repositorio)

    @Test
    fun `debe llamar al metodo borrarTodo del repositorio`() = runTest {
        useCase()
        verify(repositorio).borrarTodo()
    }
}
