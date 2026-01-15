package com.alberti.memoryaid.ui.login

import com.alberti.memoryaid.data.local.SessionManager
import com.alberti.memoryaid.domain.model.UserRole
import com.alberti.memoryaid.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sessionManager: SessionManager = mock()
    private lateinit var viewModel: LoginViewModel

    @Test
    fun `entrarComoUsuario debe establecer rol USER y marcar login exitoso`() = runTest {
        // GIVEN
        viewModel = LoginViewModel(sessionManager)

        // WHEN
        viewModel.entrarComoUsuario()

        // THEN
        verify(sessionManager).setRole(UserRole.USER)
        assertTrue(viewModel.uiState.value.loginExitoso)
    }

    @Test
    fun `mostrarDialogoPin debe detectar si es primera vez cuando no hay PIN guardado`() = runTest {
        // GIVEN
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf(null)) // No hay PIN
        viewModel = LoginViewModel(sessionManager)

        // WHEN
        viewModel.mostrarDialogoPin()

        // THEN
        val state = viewModel.uiState.value
        assertTrue(state.mostrarDialogoPin)
        assertTrue(state.esPrimeraVezAdmin)
        assertEquals("", state.pinInput)
        assertNull(state.errorPin)
    }

    @Test
    fun `mostrarDialogoPin debe detectar que NO es primera vez si ya hay PIN`() = runTest {
        // GIVEN
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf("1234")) // Ya existe PIN
        viewModel = LoginViewModel(sessionManager)

        // WHEN
        viewModel.mostrarDialogoPin()

        // THEN
        val state = viewModel.uiState.value
        assertTrue(state.mostrarDialogoPin)
        assertFalse(state.esPrimeraVezAdmin)
    }

    @Test
    fun `alCambiarPin debe actualizar el estado solo si longitud es valida`() = runTest {
        viewModel = LoginViewModel(sessionManager)

        viewModel.alCambiarPin("123")
        assertEquals("123", viewModel.uiState.value.pinInput)

        viewModel.alCambiarPin("1234")
        assertEquals("1234", viewModel.uiState.value.pinInput)

        viewModel.alCambiarPin("12345")
        assertEquals("1234", viewModel.uiState.value.pinInput) // Se queda con el anterior valido
    }

    @Test
    fun `ejecutarAccionAdmin (Primera Vez) debe guardar PIN si es valido`() = runTest {
        // GIVEN
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf(null))
        viewModel = LoginViewModel(sessionManager)

        viewModel.mostrarDialogoPin()
        viewModel.alCambiarPin("9999")

        // WHEN
        viewModel.ejecutarAccionAdmin()

        // THEN
        verify(sessionManager).guardarPin("9999")
        assertTrue(viewModel.uiState.value.loginExitoso)
        assertFalse(viewModel.uiState.value.mostrarDialogoPin)
    }

    @Test
    fun `ejecutarAccionAdmin (Primera Vez) debe mostrar error si PIN incompleto`() = runTest {
        // GIVEN
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf(null))
        viewModel = LoginViewModel(sessionManager)

        viewModel.mostrarDialogoPin()
        viewModel.alCambiarPin("12") // Solo 2 digitos

        // WHEN
        viewModel.ejecutarAccionAdmin()

        // THEN
        assertEquals("El PIN debe tener 4 dígitos", viewModel.uiState.value.errorPin)
        assertFalse(viewModel.uiState.value.loginExitoso)
    }

    @Test
    fun `ejecutarAccionAdmin (Login) debe ser exitoso con PIN correcto`() = runTest {
        // GIVEN
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf("1234"))
        whenever(sessionManager.loginComoAdmin("1234")).thenReturn(true)

        viewModel = LoginViewModel(sessionManager)
        viewModel.mostrarDialogoPin()
        viewModel.alCambiarPin("1234")

        // WHEN
        viewModel.ejecutarAccionAdmin()

        // THEN
        assertTrue(viewModel.uiState.value.loginExitoso)
        assertFalse(viewModel.uiState.value.mostrarDialogoPin)
    }

    @Test
    fun `ejecutarAccionAdmin (Login) debe mostrar error con PIN incorrecto`() = runTest {
        // GIVEN
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf("1234"))
        whenever(sessionManager.loginComoAdmin("0000")).thenReturn(false)

        viewModel = LoginViewModel(sessionManager)
        viewModel.mostrarDialogoPin()
        viewModel.alCambiarPin("0000")

        // WHEN
        viewModel.ejecutarAccionAdmin()

        // THEN
        assertEquals("PIN incorrecto", viewModel.uiState.value.errorPin)
        assertFalse(viewModel.uiState.value.loginExitoso)
    }

    @Test
    fun `ocultarDialogoPin debe cerrar el dialogo`() = runTest {
        viewModel = LoginViewModel(sessionManager)
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf(null))
        viewModel.mostrarDialogoPin()
        assertTrue(viewModel.uiState.value.mostrarDialogoPin)

        // WHEN
        viewModel.ocultarDialogoPin()

        // THEN
        assertFalse(viewModel.uiState.value.mostrarDialogoPin)
    }
}
