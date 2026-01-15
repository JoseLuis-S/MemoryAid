package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ObtenerResumenDiarioUseCaseTest {

    private val repositorio: RepositorioMemoria = mock()
    private val useCase = ObtenerResumenDiarioUseCase(repositorio)

    @Test
    fun `debe agrupar y contar los eventos por tipo correctamente`() = runTest {
        // GIVEN
        val eventos = listOf(
            EventoMemoria(titulo = "Med 1", descripcion = "", fechaHora = 0L, tipo = TipoEvento.MEDICACION),
            EventoMemoria(titulo = "Med 2", descripcion = "", fechaHora = 0L, tipo = TipoEvento.MEDICACION),
            EventoMemoria(titulo = "Crisis 1", descripcion = "", fechaHora = 0L, tipo = TipoEvento.CRISIS_CONDUCTA),
            EventoMemoria(titulo = "Nota 1", descripcion = "", fechaHora = 0L, tipo = null) // Caso null
        )

        whenever(repositorio.obtenerEventosPorRango(any(), any())).thenReturn(flowOf(eventos))

        // WHEN
        val mapaResumen = useCase(System.currentTimeMillis()).first()

        // THEN
        assertEquals(2, mapaResumen[TipoEvento.MEDICACION])
        assertEquals(1, mapaResumen[TipoEvento.CRISIS_CONDUCTA])
        assertEquals(1, mapaResumen[TipoEvento.NOTAS_GENERALES]) // El null se mapea a NOTAS_GENERALES
    }

    @Test
    fun `si no hay eventos debe devolver mapa vacio`() = runTest {
        // GIVEN
        whenever(repositorio.obtenerEventosPorRango(any(), any())).thenReturn(flowOf(emptyList()))

        // WHEN
        val mapaResumen = useCase(System.currentTimeMillis()).first()

        // THEN
        assertEquals(0, mapaResumen.size)
    }
}
