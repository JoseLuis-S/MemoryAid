package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ObtenerEstadisticasUseCaseTest {

    private val repositorio: RepositorioMemoria = mock()
    private val useCase = ObtenerEstadisticasUseCase(repositorio)

    @Test
    fun `debe calcular correctamente las estadisticas y la tendencia positiva`() = runTest {
        // GIVEN
        whenever(repositorio.contarPorTipo(eq(TipoEvento.CRISIS_CONDUCTA), any(), any()))
            .thenReturn(10) // Crisis Actual
            .thenReturn(5)  // Crisis Previa (Semana anterior)

        whenever(repositorio.contarPorTipo(eq(TipoEvento.MEDICACION), any(), any()))
            .thenReturn(20)

        whenever(repositorio.contarTotal(any(), any()))
            .thenReturn(50)

        // WHEN
        val resultado = useCase()

        // THEN
        assertEquals(20, resultado.medicinasEstaSemana)
        assertEquals(10, resultado.crisisEstaSemana)
        assertEquals(50, resultado.notasEstaSemana)
        // (10 - 5) / 5 = 100% de incremento
        assertEquals("↑ 100%", resultado.tendenciaCrisis)
    }

    @Test
    fun `debe calcular tendencia negativa correctamente`() = runTest {
        // GIVEN
        whenever(repositorio.contarPorTipo(eq(TipoEvento.CRISIS_CONDUCTA), any(), any()))
            .thenReturn(5)  // Crisis Actual
            .thenReturn(10) // Crisis Previa

        // WHEN
        val resultado = useCase()

        // THEN
        // (5 - 10) / 10 = -50%
        assertEquals("↓ 50%", resultado.tendenciaCrisis)
    }

    @Test
    fun `cuando no hay crisis previas debe indicar sin datos`() = runTest {
        // GIVEN
        whenever(repositorio.contarPorTipo(eq(TipoEvento.CRISIS_CONDUCTA), any(), any()))
            .thenReturn(5) // Actual
            .thenReturn(0) // Previa

        // WHEN
        val resultado = useCase()

        // THEN
        assertEquals("Sin datos previos", resultado.tendenciaCrisis)
    }
}
