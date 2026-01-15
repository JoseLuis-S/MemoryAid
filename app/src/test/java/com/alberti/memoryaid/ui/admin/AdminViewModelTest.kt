package com.alberti.memoryaid.ui.admin

import com.alberti.memoryaid.data.local.SessionManager
import com.alberti.memoryaid.domain.model.EstadisticasAdmin
import com.alberti.memoryaid.domain.usecase.GenerarInformeUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerEstadisticasUseCase
import com.alberti.memoryaid.domain.usecase.PurgarDatosUseCase
import com.alberti.memoryaid.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AdminViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val obtenerEstadisticasUseCase: ObtenerEstadisticasUseCase = mock()
    private val purgarDatosUseCase: PurgarDatosUseCase = mock()
    private val generarInformeUseCase: GenerarInformeUseCase = mock()
    private val sessionManager: SessionManager = mock()

    private lateinit var viewModel: AdminViewModel

    private val estadisticasVacias = EstadisticasAdmin(0, 0, 0, "")

    @Before
    fun setup() {
        runTest {
            whenever(obtenerEstadisticasUseCase()).thenReturn(estadisticasVacias)
        }
    }

    @Test
    fun `al iniciar el ViewModel debe cargar las estadisticas exitosamente`() = runTest {
        // GIVEN
        val estadisticasReales = EstadisticasAdmin(10, 5, 20, "↑ 50%")
        whenever(obtenerEstadisticasUseCase()).thenReturn(estadisticasReales)

        // WHEN
        viewModel = AdminViewModel(obtenerEstadisticasUseCase, purgarDatosUseCase, generarInformeUseCase, sessionManager)

        // THEN
        val estado = viewModel.uiState.value
        assertEquals(estadisticasReales, estado.estadisticas)
        assertEquals(false, estado.estaCargando)
        assertNull(estado.mensajeError)
    }

    @Test
    fun `si falla la carga de estadisticas debe mostrar mensaje de error`() = runTest {
        // GIVEN
        whenever(obtenerEstadisticasUseCase()).thenThrow(RuntimeException("Error DB"))

        // WHEN
        viewModel = AdminViewModel(obtenerEstadisticasUseCase, purgarDatosUseCase, generarInformeUseCase, sessionManager)

        // THEN
        assertEquals("Error al cargar datos", viewModel.uiState.value.mensajeError)
        assertEquals(false, viewModel.uiState.value.estaCargando)
    }

    @Test
    fun `al cerrar sesion debe llamar al session manager`() = runTest {
        // GIVEN
        inicializarViewModel()

        // WHEN
        viewModel.cerrarSesion()

        // THEN
        verify(sessionManager).logout()
    }

    @Test
    fun `al purgar datos debe ejecutar usecase y recargar estadisticas`() = runTest {
        // GIVEN
        inicializarViewModel()

        // WHEN
        viewModel.purgarBaseDeDatos()

        // THEN
        verify(purgarDatosUseCase).invoke()
        verify(obtenerEstadisticasUseCase, times(2)).invoke()
    }

    @Test
    fun `mostrarDialogoPurga debe actualizar el estado correctamente`() = runTest {
        inicializarViewModel()

        viewModel.mostrarDialogoPurga(true)
        assertEquals(true, viewModel.uiState.value.mostrarConfirmacionPurga)

        viewModel.mostrarDialogoPurga(false)
        assertEquals(false, viewModel.uiState.value.mostrarConfirmacionPurga)
    }

    @Test
    fun `exportarInforme debe generar texto y actualizar estado`() = runTest {
        // GIVEN
        val informeFake = "Informe Clinico Generado..."
        whenever(generarInformeUseCase()).thenReturn(informeFake)
        inicializarViewModel()

        // WHEN
        viewModel.exportarInforme()

        // THEN
        verify(generarInformeUseCase).invoke()
        assertEquals(informeFake, viewModel.uiState.value.informeGenerado)
        assertEquals(false, viewModel.uiState.value.estaCargando)
    }

    @Test
    fun `informeConsumido debe limpiar el estado del informe`() = runTest {
        // GIVEN
        inicializarViewModel()
        viewModel.exportarInforme()

        // WHEN
        viewModel.informeConsumido()

        // THEN
        assertNull(viewModel.uiState.value.informeGenerado)
    }

    private fun inicializarViewModel() {
        viewModel = AdminViewModel(obtenerEstadisticasUseCase, purgarDatosUseCase, generarInformeUseCase, sessionManager)
    }
}
