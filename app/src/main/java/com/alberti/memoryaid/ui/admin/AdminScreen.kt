package com.alberti.memoryaid.ui.admin

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alberti.memoryaid.ui.components.GraficoComparativo
import com.alberti.memoryaid.ui.components.StatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.informeGenerado) {
        state.informeGenerado?.let { texto ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Informe Clínico MemoryAid")
                putExtra(Intent.EXTRA_TEXT, texto)
            }
            context.startActivity(Intent.createChooser(intent, "Compartir informe vía"))
            viewModel.informeConsumido()
        }
    }

    if (state.mostrarDialogoPin) {
        AlertDialog(
            onDismissRequest = { if (!state.necesitaConfiguracion) viewModel.mostrarDialogoPin(false) },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = if (state.necesitaConfiguracion) "Configuración Inicial" else "Seguridad de Acceso",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (state.necesitaConfiguracion)
                            "Establece un PIN de 4 dígitos para proteger el panel de administración."
                        else "Ingresa el nuevo PIN de 4 dígitos para proteger el panel.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = state.nuevoPinInput,
                        onValueChange = { viewModel.alCambiarNuevoPin(it) },
                        label = { Text("Nuevo PIN") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = state.errorValidacion != null,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    state.errorValidacion?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.confirmarCambioPin() }) { Text("GUARDAR") }
            },
            dismissButton = {
                TextButton(onClick = {
                    if (state.necesitaConfiguracion) onBack() else viewModel.mostrarDialogoPin(false)
                }) { Text(if (state.necesitaConfiguracion) "VOLVER" else "CANCELAR") }
            }
        )
    }

    if (state.mostrarDialogoEmergencia) {
        AlertDialog(
            onDismissRequest = { viewModel.mostrarDialogoEmergencia(false) },
            title = { Text("Configurar Emergencia") },
            text = {
                OutlinedTextField(
                    value = state.nuevoEmergenciaInput,
                    onValueChange = { viewModel.alCambiarNuevoEmergencia(it) },
                    label = { Text("Número de teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = { Button(onClick = { viewModel.confirmarCambioEmergencia() }) { Text("ACTUALIZAR") } },
            dismissButton = { TextButton(onClick = { viewModel.mostrarDialogoEmergencia(false) }) { Text("CANCELAR") } }
        )
    }

    if (state.mostrarConfirmacionPurga) {
        AlertDialog(
            onDismissRequest = { viewModel.mostrarDialogoPurga(false) },
            title = { Text("¿Vaciar base de datos?") },
            text = { Text("Esta acción eliminará permanentemente todos los registros.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.purgarBaseDeDatos() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("CONFIRMAR PURGA") }
            },
            dismissButton = { TextButton(onClick = { viewModel.mostrarDialogoPurga(false) }) { Text("CANCELAR") } }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Panel de Control", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.cerrarSesion(); onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.estaCargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.necesitaConfiguracion -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Acceso restringido",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Métricas de Cuidado",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatCard(
                                titulo = "Medicinas",
                                valor = state.estadisticas.medicinasEstaSemana.toString(),
                                modifier = Modifier.weight(1f),
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                            StatCard(
                                titulo = "Crisis",
                                valor = state.estadisticas.crisisEstaSemana.toString(),
                                subtitulo = state.estadisticas.tendenciaCrisis,
                                modifier = Modifier.weight(1f),
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text("Actividad Semanal", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                                GraficoComparativo(
                                    medicinas = state.estadisticas.medicinasEstaSemana,
                                    crisis = state.estadisticas.crisisEstaSemana,
                                    modifier = Modifier.fillMaxWidth().height(200.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                        Button(
                            onClick = { viewModel.exportarInforme() },
                            modifier = Modifier.fillMaxWidth().height(64.dp),
                            shape = RoundedCornerShape(20.dp),
                            enabled = !state.estaCargando
                        ) {
                            Icon(Icons.Default.Share, null)
                            Spacer(Modifier.width(12.dp))
                            Text("GENERAR INFORME CLÍNICO", fontWeight = FontWeight.Bold)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(onClick = { viewModel.mostrarDialogoEmergencia(true) }, modifier = Modifier.weight(1f).height(56.dp)) {
                                Icon(Icons.Default.Phone, null); Spacer(Modifier.width(8.dp)); Text("TELÉFONO")
                            }
                            OutlinedButton(onClick = { viewModel.mostrarDialogoPin(true) }, modifier = Modifier.weight(1f).height(56.dp)) {
                                Icon(Icons.Default.Lock, null); Spacer(Modifier.width(8.dp)); Text("PIN ADMIN")
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text("Zona de Peligro", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onErrorContainer)
                                Spacer(Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.mostrarDialogoPurga(true) },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) { Text("PURGAR TODOS LOS REGISTROS") }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
