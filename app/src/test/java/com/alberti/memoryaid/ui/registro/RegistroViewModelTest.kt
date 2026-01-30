package com.alberti.memoryaid.ui.registro

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import com.alberti.memoryaid.domain.usecase.ActualizarEventoUseCase
import com.alberti.memoryaid.domain.usecase.AgregarEventoUseCase
import com.alberti.memoryaid.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class RegistroViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val agregarEventoUseCase: AgregarEventoUseCase = mock()
    private val actualizarEventoUseCase: ActualizarEventoUseCase = mock()
    private val repositorio: RepositorioMemoria = mock()
    private val context: Context = mock()

    private lateinit var viewModel: RegistroViewModel

    private fun createViewModel(eventoId: Long? = null) {
        val savedStateHandle = if (eventoId != null) {
            SavedStateHandle(mapOf("eventoId" to eventoId))
        } else {
            SavedStateHandle()
        }

        viewModel = RegistroViewModel(
            agregarEventoUseCase,
            actualizarEventoUseCase,
            repositorio,
            context,
            savedStateHandle
        )
    }

    @Test
    fun `init debe cargar datos del evento si eventoId esta presente`() = runTest {
        // GIVEN
        val id = 1L
        val eventoMock = EventoMemoria(
            id = id,
            titulo = "Cena",
            descripcion = "Tomar pastilla",
            fechaHora = 123456789L,
            tipo = TipoEvento.ALIMENTACION
        )
        whenever(repositorio.obtenerEventoPorId(id)).thenReturn(eventoMock)

        // WHEN
        createViewModel(eventoId = id)
        runCurrent()

        // THEN
        val state = viewModel.state.value
        assertTrue(state.esEdicion)
        assertEquals("Cena", state.titulo)
        assertEquals(TipoEvento.ALIMENTACION, state.tipo)
        verify(repositorio).obtenerEventoPorId(id)
    }

    @Test
    fun `onTituloChanged debe actualizar el estado y limpiar errores`() = runTest {
        createViewModel()

        viewModel.onTituloChanged("Nuevo Titulo")

        assertEquals("Nuevo Titulo", viewModel.state.value.titulo)
        assertEquals(null, viewModel.state.value.error)
    }

    @Test
    fun `guardarEvento en modo creacion debe llamar a AgregarEventoUseCase`() = runTest {
        // GIVEN
        createViewModel()
        viewModel.onTituloChanged("Test")
        whenever(agregarEventoUseCase(any(), any(), any(), any(), any(), any(), any())).thenReturn(Result.success(1L))

        // WHEN
        viewModel.guardarEvento()
        runCurrent()

        // THEN
        verify(agregarEventoUseCase).invoke(
            titulo = eq("Test"),
            descripcion = any(),
            fechaHora = any(),
            tipo = any(),
            recordatorioActivo = any(),
            fechaRecordatorio = any(),
            frecuenciaHoras = any()
        )
        assertTrue(viewModel.state.value.mostrarAnimacionExito)

        advanceUntilIdle()
        assertTrue(viewModel.state.value.registroExitoso)
    }

    @Test
    fun `guardarEvento en modo edicion debe llamar a ActualizarEventoUseCase`() = runTest {
        // GIVEN
        val id = 5L
        val eventoExistente = EventoMemoria(id = id, titulo = "Viejo", descripcion = "", fechaHora = 0L)
        whenever(repositorio.obtenerEventoPorId(id)).thenReturn(eventoExistente)
        whenever(actualizarEventoUseCase(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(Result.success(Unit))

        createViewModel(eventoId = id)
        runCurrent()

        viewModel.onTituloChanged("Actualizado")

        // WHEN
        viewModel.guardarEvento()
        runCurrent()

        // THEN
        verify(actualizarEventoUseCase).invoke(
            id = eq(id),
            titulo = eq("Actualizado"),
            descripcion = any(),
            fechaHora = any(),
            tipo = any(),
            recordatorioActivo = any(),
            fechaRecordatorio = any(),
            frecuenciaHoras = any()
        )
    }

    @Test
    fun `guardarEvento debe manejar errores del caso de uso`() = runTest {
        // GIVEN
        createViewModel()
        val mensajeError = "Error de base de datos"
        whenever(agregarEventoUseCase(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(Result.failure(Exception(mensajeError)))

        // WHEN
        viewModel.guardarEvento()
        runCurrent()

        // THEN
        assertFalse(viewModel.state.value.estaGuardando)
        assertEquals(mensajeError, viewModel.state.value.error)
    }

    @Test
    fun `onDateSelected debe cerrar date picker y abrir time picker`() = runTest {
        createViewModel()

        viewModel.onDateSelected(System.currentTimeMillis())

        val state = viewModel.state.value
        assertFalse(state.mostrarDatePicker)
        assertTrue(state.mostrarTimePicker)
    }
}
