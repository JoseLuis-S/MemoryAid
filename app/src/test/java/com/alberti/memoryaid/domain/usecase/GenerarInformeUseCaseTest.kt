package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GenerarInformeUseCaseTest {

    private val repositorio: RepositorioMemoria = mock()
    private val useCase = GenerarInformeUseCase(repositorio)

    @Test
    fun `cuando no hay eventos debe retornar el mensaje de historial vacio`() = runTest {
        whenever(repositorio.obtenerTodosLosEventos()).thenReturn(flowOf(emptyList()))

        val informe = useCase()

        assertTrue(informe.contains("No hay registros en el historial"))
    }

    @Test
    fun `cuando hay eventos debe retornar el informe con los datos formateados`() = runTest {
        val eventos = listOf(
            EventoMemoria(
                titulo = "Pastilla",
                descripcion = "8mg",
                fechaHora = 1705334400000L,
                tipo = TipoEvento.MEDICACION
            )
        )
        whenever(repositorio.obtenerTodosLosEventos()).thenReturn(flowOf(eventos))

        val informe = useCase()

        assertTrue("Debe contener el título", informe.contains("Pastilla"))
        assertTrue("Debe contener la descripción", informe.contains("8mg"))
        assertTrue("Debe contener el nombre del tipo", informe.contains("Medicación"))
    }
}