package com.alberti.memoryaid.ui.admin

import com.alberti.memoryaid.data.local.SessionManager
import com.alberti.memoryaid.domain.model.EstadisticasAdmin
import com.alberti.memoryaid.domain.usecase.GenerarInformeUseCase
import com.alberti.memoryaid.domain.usecase.ObtenerEstadisticasUseCase
import com.alberti.memoryaid.domain.usecase.PurgarDatosUseCase
import com.alberti.memoryaid.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AdminViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sessionManager: SessionManager = mock()
    private val obtenerEstadisticasUseCase: ObtenerEstadisticasUseCase = mock()
    private val generarInformeUseCase: GenerarInformeUseCase = mock()
    private val purgarDatosUseCase: PurgarDatosUseCase = mock()

    private lateinit var viewModel: AdminViewModel

    private val mockStats = EstadisticasAdmin(
        medicinasEstaSemana = 10,
        crisisEstaSemana = 2,
        tendenciaCrisis = "Estable"
    )

    @Before
    fun setup() {
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf("1234"))
        runTest {
            whenever(obtenerEstadisticasUseCase.invoke()).thenReturn(mockStats)
        }
    }

    @Test
    fun `init debe cargar estadisticas si el PIN ya existe`() = runTest {
        viewModel = AdminViewModel(
            sessionManager,
            obtenerEstadisticasUseCase,
            generarInformeUseCase,
            purgarDatosUseCase
        )
        runCurrent()

        val state = viewModel.uiState.value
        Assert.assertFalse(state.necesitaConfiguracion)
        Assert.assertEquals(mockStats, state.estadisticas)
        verify(obtenerEstadisticasUseCase).invoke()
    }

    @Test
    fun `init debe pedir configuracion si el PIN es nulo o vacio`() = runTest {
        whenever(sessionManager.obtenerPin()).thenReturn(flowOf(null))

        viewModel = AdminViewModel(
            sessionManager,
            obtenerEstadisticasUseCase,
            generarInformeUseCase,
            purgarDatosUseCase
        )
        runCurrent()

        val state = viewModel.uiState.value
        Assert.assertTrue(state.necesitaConfiguracion)
        Assert.assertTrue(state.mostrarDialogoPin)
    }

    @Test
    fun `confirmarCambioPin debe mostrar error si el PIN tiene menos de 4 digitos`() = runTest {
        viewModel = AdminViewModel(
            sessionManager,
            obtenerEstadisticasUseCase,
            generarInformeUseCase,
            purgarDatosUseCase
        )
        viewModel.alCambiarNuevoPin("123")

        viewModel.confirmarCambioPin()

        Assert.assertEquals("El PIN debe tener 4 dígitos", viewModel.uiState.value.errorValidacion)
        verifyNoInteractions(purgarDatosUseCase)
    }

    @Test
    fun `confirmarCambioPin exitoso debe guardar PIN y recargar datos`() = runTest {
        viewModel = AdminViewModel(
            sessionManager,
            obtenerEstadisticasUseCase,
            generarInformeUseCase,
            purgarDatosUseCase
        )
        viewModel.alCambiarNuevoPin("4321")

        viewModel.confirmarCambioPin()
        runCurrent()

        verify(sessionManager).guardarPin("4321")
        verify(obtenerEstadisticasUseCase).invoke()
        Assert.assertFalse(viewModel.uiState.value.mostrarDialogoPin)
    }

    @Test
    fun `exportarInforme debe actualizar el estado con el texto generado`() = runTest {
        val textoInforme = "Informe Clinico"
        whenever(generarInformeUseCase()).thenAnswer { textoInforme }
        viewModel = AdminViewModel(
            sessionManager,
            obtenerEstadisticasUseCase,
            generarInformeUseCase,
            purgarDatosUseCase
        )

        viewModel.exportarInforme()
        runCurrent()

        Assert.assertEquals(textoInforme, viewModel.uiState.value.informeGenerado)
        Assert.assertFalse(viewModel.uiState.value.estaCargando)
    }

    @Test
    fun `purgarBaseDeDatos debe llamar al caso de uso y cerrar dialogo`() = runTest {
        viewModel = AdminViewModel(
            sessionManager,
            obtenerEstadisticasUseCase,
            generarInformeUseCase,
            purgarDatosUseCase
        )

        viewModel.purgarBaseDeDatos()
        runCurrent()

        verify(purgarDatosUseCase).invoke()
        Assert.assertFalse(viewModel.uiState.value.mostrarConfirmacionPurga)
    }

    @Test
    fun `cerrarSesion debe llamar al logout del sessionManager`() = runTest {
        viewModel = AdminViewModel(
            sessionManager,
            obtenerEstadisticasUseCase,
            generarInformeUseCase,
            purgarDatosUseCase
        )

        viewModel.cerrarSesion()
        runCurrent()

        verify(sessionManager).logout()
    }
}