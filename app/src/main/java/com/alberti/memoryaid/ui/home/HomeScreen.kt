package com.alberti.memoryaid.ui.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    alEditarEvento: (Long) -> Unit,
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

    if (estado.mostrarDialogoConfigInicial) {
        AlertDialog(
            onDismissRequest = { viewModel.ocultarDialogoConfigInicial() },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Configuración Requerida",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "No se ha detectado un PIN de administrador. Debe establecer uno por primera vez para acceder a las estadísticas.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.ocultarDialogoConfigInicial()
                        alNavegarAAdmin()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Configurar ahora") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.ocultarDialogoConfigInicial() }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.secondary)
                }
            }
        )
    }

    if (estado.mostrarDialogoConfigContacto) {
        var numTemp by remember { mutableStateOf(contactoEmergencia ?: "") }
        AlertDialog(
            onDismissRequest = { viewModel.mostrarConfigContacto(false) },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Contacto de Auxilio",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                OutlinedTextField(
                    value = numTemp,
                    onValueChange = { numTemp = it },
                    label = { Text("Número de teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.guardarContacto(numTemp) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.mostrarConfigContacto(false) }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.secondary)
                }
            }
        )
    }

    if (estado.mostrarDialogoPin) {
        AlertDialog(
            onDismissRequest = { viewModel.ocultarDialogoPin() },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Área de Gestión",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Introduce el PIN de seguridad para acceder a las estadísticas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = estado.pinInput,
                        onValueChange = { viewModel.alCambiarPin(it) },
                        label = { Text("PIN de Acceso") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = estado.errorPin != null,
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    estado.errorPin?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.validarPinAdmin() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("Validar") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.ocultarDialogoPin() }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.secondary)
                }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "MemoryAid",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.alClickAdmin() },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Gestión",
                                modifier = Modifier.padding(8.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = alNavegarARegistro,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Default.Add, "Nuevo Registro", modifier = Modifier.size(36.dp))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp),
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
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.1f),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (contactoEmergencia.isNullOrBlank()) "Configurar Auxilio" else "AVISO DE EMERGENCIA",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = if (contactoEmergencia.isNullOrBlank()) "Añade un número de contacto" else contactoEmergencia!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ResumenDiarioWidget(resumen = resumen)
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BuscadorBar(
                        query = textoBusqueda,
                        alCambiarQuery = { viewModel.alCambiarBusqueda(it) }
                    )
                    FiltrosSeccion(
                        seleccionado = estado.filtroSeleccionado,
                        alSeleccionar = { viewModel.alCambiarFiltro(it) }
                    )
                }
            }

            if (listaEventos.isEmpty() && !estado.estaCargando) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No hay registros disponibles",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            } else {
                items(listaEventos, key = { it.id ?: 0L }) { evento ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)) {
                        EventoItem(
                            evento = evento,
                            alEliminar = { viewModel.mostrarConfirmacionBorrado(evento) },
                            alEditar = { alEditarEvento(evento.id ?: 0L) }
                        )
                    }
                }
            }
        }
    }
}
