package com.alberti.memoryaid.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EventoItem(
    evento: EventoMemoria,
    alEliminar: () -> Unit,
    alEditar: () -> Unit
) {
    val containerColor = when (evento.tipo) {
        TipoEvento.CRISIS_CONDUCTA -> MaterialTheme.colorScheme.errorContainer
        TipoEvento.MEDICACION -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val contentColor = when (evento.tipo) {
        TipoEvento.CRISIS_CONDUCTA -> MaterialTheme.colorScheme.onErrorContainer
        TipoEvento.MEDICACION -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    val icon = when (evento.tipo) {
        TipoEvento.CRISIS_CONDUCTA -> Icons.Default.Warning
        TipoEvento.MEDICACION -> Icons.Default.Medication
        else -> Icons.Default.EventNote
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { alEditar() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = contentColor
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = evento.titulo,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (evento.descripcion.isNotBlank()) {
                    Text(
                        text = evento.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor.copy(alpha = 0.8f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )
                }
                Text(
                    text = SimpleDateFormat("HH:mm - dd MMM", Locale.getDefault()).format(evento.fechaHora),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    ),
                    color = contentColor.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            IconButton(
                onClick = alEliminar,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = contentColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}
