package com.alberti.memoryaid.ui.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val context = LocalContext.current
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val listaEventos by viewModel.eventos.collectAsStateWithLifecycle()
    val resumen by viewModel.resumenDiario.collectAsStateWithLifecycle()
    val textoBusqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val eventoABorrar by viewModel.eventoABorrar.collectAsStateWithLifecycle()
    val contactoEmergencia by viewModel.contactoEmergencia.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && !contactoEmergencia.isNullOrBlank()) {
            viewModel.realizarLlamadaDirecta(context, contactoEmergencia!!)
        }
    }

    LaunchedEffect(estado.navegarAAdmin) {
        if (estado.navegarAAdmin) {
            alNavegarAAdmin()
            viewModel.resetNavegacionAdmin()
        }
    }

    eventoABorrar?.let { evento ->
        ConfirmacionBorradoDialog(
            nombreEvento = evento.titulo,
            onConfirmar = { viewModel.confirmarBorrado() },
            onDescartar = { viewModel.cancelarBorrado() }
        )
    }

    if (estado.mostrarDialogoConfigContacto) {
        var numTemp by remember { mutableStateOf(contactoEmergencia ?: "") }
        AlertDialog(
            onDismissRequest = { viewModel.mostrarConfigContacto(false) },
            title = { Text("Configurar Emergencia") },
            text = {
                OutlinedTextField(
                    value = numTemp,
                    onValueChange = { numTemp = it },
                    label = { Text("Número de teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = { viewModel.guardarContacto(numTemp) }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.mostrarConfigContacto(false) }) { Text("Cancelar") }
            }
        )
    }

    if (estado.mostrarDialogoPin) {
        AlertDialog(
            onDismissRequest = { viewModel.ocultarDialogoPin() },
            title = { Text("Acceso Restringido") },
            text = {
                Column {
                    Text("Introduce el PIN de administrador para continuar.")
                    OutlinedTextField(
                        value = estado.pinInput,
                        onValueChange = { viewModel.alCambiarPin(it) },
                        label = { Text("PIN") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = estado.errorPin != null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    estado.errorPin?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.validarPinAdmin() }) { Text("Entrar") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.ocultarDialogoPin() }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MemoryAid") },
                actions = {
                    IconButton(onClick = { viewModel.alClickAdmin() }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configuración Admin",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                onClick = {
                    if (contactoEmergencia.isNullOrBlank()) {
                        viewModel.mostrarConfigContacto(true)
                    } else {
                        val hasPerm = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CALL_PHONE
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasPerm) {
                            viewModel.realizarLlamadaDirecta(context, contactoEmergencia!!)
                        } else {
                            permissionLauncher.launch(Manifest.permission.CALL_PHONE)
                        }
                    }
                }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (contactoEmergencia.isNullOrBlank()) "Configurar Emergencia" else "LLAMADA DE EMERGENCIA",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        if (!contactoEmergencia.isNullOrBlank()) {
                            Text(
                                text = contactoEmergencia!!,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            ResumenDiarioWidget(resumen = resumen)

            BuscadorBar(
                query = textoBusqueda,
                alCambiarQuery = { viewModel.alCambiarBusqueda(it) }
            )

            FiltrosSeccion(
                seleccionado = estado.filtroSeleccionado,
                alSeleccionar = { viewModel.alCambiarFiltro(it) }
            )

            if (listaEventos.isEmpty() && !estado.estaCargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron eventos")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listaEventos) { evento ->
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
