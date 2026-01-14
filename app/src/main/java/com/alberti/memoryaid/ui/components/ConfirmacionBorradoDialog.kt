package com.alberti.memoryaid.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
fun ConfirmacionBorradoDialog(
    nombreEvento: String,
    onConfirmar: () -> Unit,
    onDescartar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDescartar,
        icon = {
            Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
        },
        title = {
            Text(text = "¿Borrar registro?")
        },
        text = {
            Text(text = "Estás a punto de eliminar '$nombreEvento'. Esta acción no se puede deshacer.")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmar,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDescartar) {
                Text("Cancelar")
            }
        }
    )
}