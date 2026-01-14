package com.alberti.memoryaid.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento

@Composable
fun EventoItem(
    evento: EventoMemoria,
    alEliminar: () -> Unit
) {
    val colorTipo = when (evento.tipo) {
        TipoEvento.CRISIS_CONDUCTA -> MaterialTheme.colorScheme.errorContainer
        TipoEvento.MEDICACION -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorTipo)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = evento.titulo, style = MaterialTheme.typography.titleLarge)
                Text(text = evento.descripcion, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = java.text.SimpleDateFormat("HH:mm - dd MMM", java.util.Locale.getDefault())
                        .format(evento.fechaHora),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            IconButton(onClick = alEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}