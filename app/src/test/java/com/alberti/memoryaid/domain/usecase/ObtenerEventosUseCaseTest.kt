package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ObtenerEventosUseCaseTest {

    private val repositorio: RepositorioMemoria = mock()
    private val useCase = ObtenerEventosUseCase(repositorio)

    @Test
    fun `debe llamar al repositorio con los filtros correctos`() = runTest {
        // GIVEN
        val listaMock = listOf(EventoMemoria(titulo = "Test", descripcion = "", fechaHora = 0L))
        val tipoFiltro = TipoEvento.MEDICACION
        val queryFiltro = "Aspirina"

        whenever(repositorio.obtenerEventos(tipoFiltro, queryFiltro)).thenReturn(flowOf(listaMock))

        // WHEN
        val resultado = useCase(tipoFiltro, queryFiltro).toList()

        // THEN
        verify(repositorio).obtenerEventos(tipoFiltro, queryFiltro)
        assertEquals(1, resultado.size)
        assertEquals(listaMock, resultado[0])
    }
}
