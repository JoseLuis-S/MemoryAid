package com.alberti.memoryaid.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Componente de búsqueda personalizado con estilo Material 3.
 * * Proporciona un campo de entrada estilizado para el filtrado dinámico de eventos.
 * Incluye un icono de búsqueda decorativo y un botón de limpieza funcional que aparece
 * únicamente cuando el campo contiene texto.
 *
 * @param query El texto actual de búsqueda que se mostrará en el campo.
 * @param alCambiarQuery Callback que se dispara cada vez que el usuario modifica el texto
 * o presiona el botón de limpiar.
 */
@Composable
fun BuscadorBar(
    query: String,
    alCambiarQuery: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = alCambiarQuery,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        placeholder = {
            Text(
                "Buscar notas, medicinas...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null, // Decorativo, no requiere descripción para accesibilidad
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            // El botón de limpieza solo se muestra si hay contenido
            if (query.isNotEmpty()) {
                IconButton(onClick = { alCambiarQuery("") }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Limpiar búsqueda",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        shape = RoundedCornerShape(28.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
