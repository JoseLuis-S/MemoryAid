package com.alberti.memoryaid.ui.registro

import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.usecase.AgregarEventoUseCase
import com.alberti.memoryaid.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class RegistroViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val agregarEventoUseCase: AgregarEventoUseCase = mock()
    private lateinit var viewModel: RegistroViewModel

    @Before
    fun setup() {
        viewModel = RegistroViewModel(agregarEventoUseCase)
    }

    @Test
    fun `actualizar campos debe reflejarse inmediatamente en el UI State`() = runTest {
        // WHEN
        viewModel.onTituloChanged("Nuevo Titulo")
        viewModel.onDescripcionChanged("Nueva Descripcion")
        viewModel.onTipoChanged(TipoEvento.MEDICACION)
        viewModel.onFechaHoraChanged(123456789L)

        // THEN
        val state = viewModel.state.value
        assertEquals("Nuevo Titulo", state.titulo)
        assertEquals("Nueva Descripcion", state.descripcion)
        assertEquals(TipoEvento.MEDICACION, state.tipo)
        assertEquals(123456789L, state.fechaHora)
    }

    @Test
    fun `guardarEvento exitoso debe marcar registroExitoso como true`() = runTest {
        // GIVEN
        viewModel.onTituloChanged("Evento Correcto")
        whenever(agregarEventoUseCase(any(), any(), any(), any())).thenReturn(Result.success(Unit))

        // WHEN
        viewModel.guardarEvento()

        // THEN
        val state = viewModel.state.value
        assertFalse(state.estaGuardando)
        assertTrue(state.registroExitoso)
        assertNull(state.error)
        verify(agregarEventoUseCase).invoke(any(), any(), any(), any())
    }

    @Test
    fun `guardarEvento fallido debe mostrar mensaje de error en el estado`() = runTest {
        // GIVEN
        val mensajeError = "El título no puede estar vacío"
        whenever(agregarEventoUseCase(any(), any(), any(), any()))
            .thenReturn(Result.failure(Exception(mensajeError)))

        // WHEN
        viewModel.guardarEvento()

        // THEN
        val state = viewModel.state.value
        assertFalse(state.estaGuardando)
        assertFalse(state.registroExitoso)
        assertEquals(mensajeError, state.error)
    }

    @Test
    fun `al cambiar titulo debe limpiarse el error previo`() = runTest {
        whenever(agregarEventoUseCase(any(), any(), any(), any()))
            .thenReturn(Result.failure(Exception("Error")))
        viewModel.guardarEvento()

        assertEquals("Error", viewModel.state.value.error)

        // WHEN
        viewModel.onTituloChanged("H")

        // THEN
        assertNull(viewModel.state.value.error)
    }
}
