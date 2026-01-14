package com.alberti.memoryaid.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alberti.memoryaid.domain.model.UserRole
import com.alberti.memoryaid.ui.components.BuscadorBar
import com.alberti.memoryaid.ui.components.ConfirmacionBorradoDialog
import com.alberti.memoryaid.ui.components.EventoItem
import com.alberti.memoryaid.ui.components.FiltrosSeccion
import com.alberti.memoryaid.ui.components.ResumenDiarioWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    alNavegarARegistro: () -> Unit,
    alNavegarAAdmin: () -> Unit = {}
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val resumen by viewModel.resumenDiario.collectAsStateWithLifecycle()
    val textoBusqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val eventoABorrar by viewModel.eventoABorrar.collectAsStateWithLifecycle()
    val rol by viewModel.role.collectAsStateWithLifecycle()

    eventoABorrar?.let { evento ->
        ConfirmacionBorradoDialog(
            nombreEvento = evento.titulo,
            onConfirmar = { viewModel.confirmarBorrado() },
            onDescartar = { viewModel.cancelarBorrado() }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MemoryAid") },
                actions = {
                    if (rol == UserRole.ADMIN) {
                        IconButton(onClick = alNavegarAAdmin) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Configuración Admin",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = alNavegarARegistro) {
                Icon(Icons.Default.Add, contentDescription = "Agregar evento")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            ResumenDiarioWidget(resumen = resumen)

            BuscadorBar(
                query = textoBusqueda,
                alCambiarQuery = { viewModel.alCambiarBusqueda(it) }
            )

            FiltrosSeccion(
                seleccionado = estado.filtroSeleccionado,
                alSeleccionar = { viewModel.alCambiarFiltro(it) }
            )

            if (estado.eventos.isEmpty() && !estado.estaCargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron eventos")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(estado.eventos) { evento ->
                        EventoItem(
                            evento = evento,
                            alEliminar = { viewModel.mostrarConfirmacionBorrado(evento) }
                        )
                    }
                }
            }
        }
    }
}