package com.alberti.memoryaid.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(
    onNavegarAHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.loginExitoso) {
        if (state.loginExitoso) onNavegarAHome()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "MemoryAid",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(
            onClick = { viewModel.entrarComoUsuario() },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("ENTRAR", style = MaterialTheme.typography.headlineLarge)
        }

        Spacer(modifier = Modifier.height(32.dp))

        TextButton(onClick = { viewModel.mostrarDialogoPin() }) {
            Text("Acceso Administrador", color = MaterialTheme.colorScheme.secondary)
        }
    }

    if (state.mostrarDialogoPin) {
        AlertDialog(
            onDismissRequest = { viewModel.ocultarDialogoPin() },
            title = {
                Text(if (state.esPrimeraVezAdmin) "Configurar PIN Admin" else "PIN de Administrador")
            },
            text = {
                Column {
                    Text(
                        text = if (state.esPrimeraVezAdmin)
                            "Cree un código de 4 dígitos para proteger las funciones de gestión."
                        else "Introduzca su código de acceso.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = state.pinInput,
                        onValueChange = { viewModel.alCambiarPin(it) },
                        label = { Text(if (state.esPrimeraVezAdmin) "Nuevo PIN" else "Introduce PIN") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.errorPin != null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (state.errorPin != null) {
                        Text(
                            text = state.errorPin!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.ejecutarAccionAdmin() }) {
                    Text(if (state.esPrimeraVezAdmin) "Guardar y Entrar" else "Validar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.ocultarDialogoPin() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
