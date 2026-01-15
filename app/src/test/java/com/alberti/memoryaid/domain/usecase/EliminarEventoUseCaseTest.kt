package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class EliminarEventoUseCaseTest {

    private val repositorio: RepositorioMemoria = mock()
    private val useCase = EliminarEventoUseCase(repositorio)

    @Test
    fun `debe llamar al repositorio para eliminar el evento`() = runTest {
        val evento = EventoMemoria("T", "D", 0L, TipoEvento.OTROS)

        useCase(evento)

        verify(repositorio).eliminarEvento(evento)
    }
}
