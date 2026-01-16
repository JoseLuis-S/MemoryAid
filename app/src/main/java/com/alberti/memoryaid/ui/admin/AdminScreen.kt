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
            onDismissRequest = { viewModel.mostrarDialogoPin(false) },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Seguridad de Acceso",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column {
                    Text(
                        text = "Ingresa el nuevo PIN de 4 dígitos para proteger el panel.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmarCambioPin() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("GUARDAR") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.mostrarDialogoPin(false) }) { Text("CANCELAR") }
            }
        )
    }

    if (state.mostrarDialogoEmergencia) {
        AlertDialog(
            onDismissRequest = { viewModel.mostrarDialogoEmergencia(false) },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Configurar Emergencia",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                OutlinedTextField(
                    value = state.nuevoEmergenciaInput,
                    onValueChange = { viewModel.alCambiarNuevoEmergencia(it) },
                    label = { Text("Número de teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmarCambioEmergencia() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("ACTUALIZAR") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.mostrarDialogoEmergencia(false) }) { Text("CANCELAR") }
            }
        )
    }

    if (state.mostrarConfirmacionPurga) {
        AlertDialog(
            onDismissRequest = { viewModel.mostrarDialogoPurga(false) },
            shape = RoundedCornerShape(28.dp),
            icon = { Icon(Icons.Default.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("¿Vaciar base de datos?") },
            text = { Text("Esta acción eliminará permanentemente todos los registros. No se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.purgarBaseDeDatos() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("CONFIRMAR PURGA") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.mostrarDialogoPurga(false) }) { Text("CANCELAR") }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Panel de Control",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.cerrarSesion()
                        onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión", tint = MaterialTheme.colorScheme.secondary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            Text(
                text = "Métricas de Cuidado",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    titulo = "Medicinas",
                    valor = state.estadisticas.medicinasEstaSemana.toString(),
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
                StatCard(
                    titulo = "Crisis",
                    valor = state.estadisticas.crisisEstaSemana.toString(),
                    subtitulo = state.estadisticas.tendenciaCrisis,
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Actividad Semanal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    GraficoComparativo(
                        medicinas = state.estadisticas.medicinasEstaSemana,
                        crisis = state.estadisticas.crisisEstaSemana,
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

            Text(
                text = "Gestión de Configuración",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Button(
                onClick = { viewModel.exportarInforme() },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                enabled = !state.estaCargando
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("GENERAR INFORME CLÍNICO", fontWeight = FontWeight.Bold)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { viewModel.mostrarDialogoEmergencia(true) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Phone, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("TELÉFONO", style = MaterialTheme.typography.labelLarge)
                }

                OutlinedButton(
                    onClick = { viewModel.mostrarDialogoPin(true) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Lock, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("PIN ADMIN", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Zona de Peligro",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "El borrado de historial es irreversible. Los datos se eliminarán permanentemente de este dispositivo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )
                    Button(
                        onClick = { viewModel.mostrarDialogoPurga(true) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text("PURGAR TODOS LOS REGISTROS", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
