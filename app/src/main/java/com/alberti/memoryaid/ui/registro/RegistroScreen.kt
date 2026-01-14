package com.alberti.memoryaid.ui.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alberti.memoryaid.ui.components.FiltrosSeccion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel = hiltViewModel(),
    onVolver: () -> Unit
) {
    val estado by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(estado.registroExitoso) {
        if (estado.registroExitoso) onVolver()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Registro") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = estado.titulo,
                onValueChange = viewModel::onTituloChanged,
                label = { Text("Título (ej: Medicación mañana)") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.error != null
            )

            OutlinedTextField(
                value = estado.descripcion,
                onValueChange = viewModel::onDescripcionChanged,
                label = { Text("Descripción o notas") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Text("Categoría del evento", style = MaterialTheme.typography.labelMedium)

            FiltrosSeccion(
                seleccionado = estado.tipo,
                alSeleccionar = viewModel::onTipoChanged
            )

            if (estado.error != null) {
                Text(text = estado.error!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = viewModel::guardarEvento,
                modifier = Modifier.fillMaxWidth(),
                enabled = !estado.estaGuardando && estado.titulo.isNotBlank()
            ) {
                if (estado.estaGuardando) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Guardar Registro")
                }
            }
        }
    }
}