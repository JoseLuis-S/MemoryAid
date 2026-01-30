package com.alberti.memoryaid.ui.home

import android.content.Context
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
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    private val context: Context = mock()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        // Configuramos los flujos básicos para que el init del ViewModel no falle
        whenever(obtenerEventosUseCase(any(), any())).thenReturn(flowOf(emptyList()))
        whenever(obtenerResumenDiarioUseCase(any())).thenReturn(flowOf(emptyMap()))
        whenever(sessionManager.rolActual).thenReturn(MutableStateFlow(UserRole.USER))
        whenever(sessionManager.contactoEmergencia).thenReturn(flowOf(null))
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf("1234"))
    }

    @Test
    fun `alCambiarBusqueda debe actualizar el flujo de busqueda`() = runTest {
        viewModel = HomeViewModel(obtenerEventosUseCase, eliminarEventoUseCase, obtenerResumenDiarioUseCase, sessionManager, context)
        val query = "Cena"

        viewModel.alCambiarBusqueda(query)

        assertEquals(query, viewModel.busqueda.value)
    }

    @Test
    fun `guardarContacto debe llamar al manager y cerrar el dialogo`() = runTest {
        viewModel = HomeViewModel(obtenerEventosUseCase, eliminarEventoUseCase, obtenerResumenDiarioUseCase, sessionManager, context)
        val numero = "600000000"

        viewModel.guardarContacto(numero)
        runCurrent() // Ejecuta la corrutina de launch

        verify(sessionManager).guardarContactoEmergencia(numero)
        assertFalse(viewModel.uiState.value.mostrarDialogoConfigContacto)
    }

    @Test
    fun `alClickAdmin debe navegar directamente si el rol ya es ADMIN`() = runTest {
        whenever(sessionManager.rolActual).thenReturn(MutableStateFlow(UserRole.ADMIN))
        viewModel = HomeViewModel(obtenerEventosUseCase, eliminarEventoUseCase, obtenerResumenDiarioUseCase, sessionManager, context)

        viewModel.alClickAdmin()
        runCurrent()

        assertTrue(viewModel.uiState.value.navegarAAdmin)
    }

    @Test
    fun `alClickAdmin debe mostrar dialogo de config inicial si no hay PIN`() = runTest {
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf(null)) // Sin PIN
        viewModel = HomeViewModel(obtenerEventosUseCase, eliminarEventoUseCase, obtenerResumenDiarioUseCase, sessionManager, context)

        viewModel.alClickAdmin()
        runCurrent()

        assertTrue(viewModel.uiState.value.mostrarDialogoConfigInicial)
    }

    @Test
    fun `alClickAdmin debe mostrar dialogo de PIN si ya existe uno`() = runTest {
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf("1234"))
        viewModel = HomeViewModel(obtenerEventosUseCase, eliminarEventoUseCase, obtenerResumenDiarioUseCase, sessionManager, context)

        viewModel.alClickAdmin()
        runCurrent()

        assertTrue(viewModel.uiState.value.mostrarDialogoPin)
        assertEquals("", viewModel.uiState.value.pinInput)
    }

    @Test
    fun `validarPinAdmin con exito debe navegar al panel de admin`() = runTest {
        whenever(sessionManager.loginComoAdmin("1234")).thenReturn(true)
        viewModel = HomeViewModel(obtenerEventosUseCase, eliminarEventoUseCase, obtenerResumenDiarioUseCase, sessionManager, context)

        viewModel.alCambiarPin("1234")
        viewModel.validarPinAdmin()
        runCurrent()

        assertTrue(viewModel.uiState.value.navegarAAdmin)
        assertFalse(viewModel.uiState.value.mostrarDialogoPin)
    }

    @Test
    fun `validarPinAdmin fallido debe mostrar mensaje de error`() = runTest {
        whenever(sessionManager.loginComoAdmin("0000")).thenReturn(false)
        viewModel = HomeViewModel(obtenerEventosUseCase, eliminarEventoUseCase, obtenerResumenDiarioUseCase, sessionManager, context)

        viewModel.alCambiarPin("0000")
        viewModel.validarPinAdmin()
        runCurrent()

        assertEquals("PIN incorrecto", viewModel.uiState.value.errorPin)
        assertFalse(viewModel.uiState.value.navegarAAdmin)
    }

    @Test
    fun `confirmarBorrado debe ejecutar el caso de uso y limpiar el estado`() = runTest {
        viewModel = HomeViewModel(obtenerEventosUseCase, eliminarEventoUseCase, obtenerResumenDiarioUseCase, sessionManager, context)
        val evento = EventoMemoria(
        id = 1L,
        titulo = "Test",
        descripcion = "Descripción detallada del evento de prueba",
        fechaHora = System.currentTimeMillis(),
        tipo = TipoEvento.ALIMENTACION,
        recordatorioActivo = false,
        fechaRecordatorio = null,
        frecuenciaHoras = 0
    )

        viewModel.mostrarConfirmacionBorrado(evento)
        viewModel.confirmarBorrado()
        runCurrent()

        verify(eliminarEventoUseCase).invoke(evento)
        assertEquals(null, viewModel.eventoABorrar.value)
    }
}
