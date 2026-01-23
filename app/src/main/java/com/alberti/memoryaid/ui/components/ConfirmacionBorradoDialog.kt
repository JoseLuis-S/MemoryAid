package com.alberti.memoryaid.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Diálogo de confirmación para acciones destructivas (eliminación).
 * * Este componente sigue los patrones de diseño de **Material 3** para diálogos de alerta,
 * proporcionando una advertencia visual clara mediante el uso de colores de error
 * y tipografía enfatizada. Se utiliza para prevenir la pérdida accidental de datos
 * requiriendo una confirmación explícita del usuario.
 * * @param nombreEvento Texto descriptivo del elemento que se va a eliminar (ej. el título de una nota).
 * @param onConfirmar Callback ejecutado cuando el usuario acepta la eliminación definitiva.
 * @param onDescartar Callback ejecutado cuando el usuario cancela la acción o cierra el diálogo.
 */
@Composable
fun ConfirmacionBorradoDialog(
    nombreEvento: String,
    onConfirmar: () -> Unit,
    onDescartar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDescartar,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(28.dp),
        icon = {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = null, // Icono decorativo que refuerza el título
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = "¿Confirmar eliminación?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = "Estás eliminando el registro '$nombreEvento'. Esta acción es permanente.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Eliminar Registro")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDescartar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar", color = MaterialTheme.colorScheme.secondary)
            }
        }
    )
}
