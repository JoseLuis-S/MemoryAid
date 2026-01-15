package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AgregarEventoUseCaseTest {

    private val repositorio: RepositorioMemoria = mock()
    private val useCase = AgregarEventoUseCase(repositorio)

    @Test
    fun `cuando el titulo esta vacio debe retornar fallo`() = runTest {
        val resultado = useCase(titulo = "", descripcion = "", fechaHora = 0L, tipo = TipoEvento.OTROS)

        assertTrue(resultado.isFailure)
        assertTrue(resultado.exceptionOrNull()?.message == "El título no puede estar vacío")
    }

    @Test
    fun `cuando los datos son correctos debe llamar al repositorio y retornar exito`() = runTest {
        val resultado = useCase(titulo = "Cena", descripcion = "En casa", fechaHora = 12345L, tipo = TipoEvento.ALIMENTACION)

        verify(repositorio).guardarEvento(any())
        assertTrue(resultado.isSuccess)
    }
}