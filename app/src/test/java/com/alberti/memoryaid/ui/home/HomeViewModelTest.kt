package com.alberti.memoryaid.ui.home

import com.alberti.memoryaid.data.local.SessionManager
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.model.UserRole
import com.alberti.memoryaid.domain.usecase.EliminarEventoUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerEventosUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerResumenDiarioUseCase
import com.alberti.memoryaid.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
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
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val obtenerEventosUseCase: ObtenerEventosUseCase = mock()
    private val eliminarEventoUseCase: EliminarEventoUseCase = mock()
    private val obtenerResumenDiarioUseCase: ObtenerResumenDiarioUseCase = mock()
    private val sessionManager: SessionManager = mock()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        whenever(obtenerResumenDiarioUseCase(any())).thenReturn(flowOf(emptyMap()))
        whenever(obtenerEventosUseCase(any(), any())).thenReturn(flowOf(emptyList()))
        whenever(sessionManager.rolActual).thenReturn(MutableStateFlow(UserRole.USER))
    }

    @Test
    fun `al iniciar debe cargar eventos y actualizar uiState`() = runTest {
        // GIVEN
        val evento = EventoMemoria(titulo = "Test", descripcion = "", fechaHora = 0L)
        whenever(obtenerEventosUseCase(any(), any())).thenReturn(flowOf(listOf(evento)))

        // WHEN
        inicializarViewModel()

        // THEN
        val eventos = viewModel.eventos.value
        val state = viewModel.uiState.value

        assertEquals(1, state.eventos.size)
        assertEquals("Test", state.eventos[0].titulo)
    }

    @Test
    fun `al cambiar filtro debe actualizar uiState`() = runTest {
        inicializarViewModel()

        viewModel.alCambiarFiltro(TipoEvento.MEDICACION)

        assertEquals(TipoEvento.MEDICACION, viewModel.uiState.value.filtroSeleccionado)
    }

    @Test
    fun `flujo de borrado debe invocar al usecase correctamente`() = runTest {
        inicializarViewModel()
        val evento = EventoMemoria(titulo = "Borrar", descripcion = "", fechaHora = 0L)

        viewModel.mostrarConfirmacionBorrado(evento)
        assertEquals(evento, viewModel.eventoABorrar.value)

        viewModel.confirmarBorrado()
        verify(eliminarEventoUseCase).invoke(evento)
        assertNull(viewModel.eventoABorrar.value)
    }

    @Test
    fun `al cancelar borrado debe limpiar el evento seleccionado`() = runTest {
        inicializarViewModel()
        val evento = EventoMemoria(titulo = "Borrar", descripcion = "", fechaHora = 0L)

        viewModel.mostrarConfirmacionBorrado(evento)
        viewModel.cancelarBorrado()

        assertNull(viewModel.eventoABorrar.value)
    }

    @Test
    fun `si el usuario ya es ADMIN debe navegar directo sin pedir PIN`() = runTest {
        // GIVEN
        whenever(sessionManager.rolActual).thenReturn(MutableStateFlow(UserRole.ADMIN))
        inicializarViewModel()

        // WHEN
        viewModel.alClickAdmin()

        // THEN
        assertTrue(viewModel.uiState.value.navegarAAdmin)
        assertFalse(viewModel.uiState.value.mostrarDialogoPin)
    }

    @Test
    fun `si el usuario es USER debe mostrar dialogo de PIN`() = runTest {
        // GIVEN
        whenever(sessionManager.rolActual).thenReturn(MutableStateFlow(UserRole.USER))
        inicializarViewModel()

        // WHEN
        viewModel.alClickAdmin()

        // THEN
        assertTrue(viewModel.uiState.value.mostrarDialogoPin)
        assertFalse(viewModel.uiState.value.navegarAAdmin)
    }

    @Test
    fun `validacion de PIN correcta debe navegar a admin`() = runTest {
        // GIVEN
        whenever(sessionManager.loginComoAdmin("1234")).thenReturn(true)
        inicializarViewModel()
        viewModel.alCambiarPin("1234")

        // WHEN
        viewModel.validarPinAdmin()

        // THEN
        assertFalse(viewModel.uiState.value.mostrarDialogoPin)
        assertTrue(viewModel.uiState.value.navegarAAdmin)
        assertNull(viewModel.uiState.value.errorPin)
    }

    @Test
    fun `validacion de PIN incorrecta debe mostrar error`() = runTest {
        // GIVEN
        whenever(sessionManager.loginComoAdmin("0000")).thenReturn(false)
        inicializarViewModel()
        viewModel.alCambiarPin("0000")

        // WHEN
        viewModel.validarPinAdmin()

        // THEN
        assertTrue(viewModel.uiState.value.mostrarDialogoPin)
        assertFalse(viewModel.uiState.value.navegarAAdmin)
        assertEquals("PIN incorrecto", viewModel.uiState.value.errorPin)
    }

    @Test
    fun `resetNavegacionAdmin debe volver el flag a false`() = runTest {
        inicializarViewModel()
        viewModel.alClickAdmin()

        whenever(sessionManager.loginComoAdmin(any())).thenReturn(true)
        viewModel.validarPinAdmin()

        assertTrue(viewModel.uiState.value.navegarAAdmin)

        // WHEN
        viewModel.resetNavegacionAdmin()

        // THEN
        assertFalse(viewModel.uiState.value.navegarAAdmin)
    }

    private fun inicializarViewModel() {
        viewModel = HomeViewModel(
            obtenerEventosUseCase,
            eliminarEventoUseCase,
            obtenerResumenDiarioUseCase,
            sessionManager
        )
    }
}