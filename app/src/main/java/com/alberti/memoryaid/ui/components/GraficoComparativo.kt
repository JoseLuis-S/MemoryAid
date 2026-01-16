package com.alberti.memoryaid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GraficoComparativo(
    medicinas: Int,
    crisis: Int,
    modifier: Modifier = Modifier
) {
    val total = (medicinas + crisis).toFloat()
    val pctMedicinas = if (total > 0) medicinas / total else 0.5f
    val pctCrisis = if (total > 0) crisis / total else 0.5f

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "DISTRIBUCIÓN SEMANAL",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            if (medicinas > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(pctMedicinas)
                        .background(MaterialTheme.colorScheme.secondary)
                )
            }
            if (crisis > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(pctCrisis)
                        .background(MaterialTheme.colorScheme.errorContainer)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LeyendaItem(
                label = "Medicinas",
                cantidad = medicinas,
                color = MaterialTheme.colorScheme.secondary
            )
            LeyendaItem(
                label = "Crisis",
                cantidad = crisis,
                color = MaterialTheme.colorScheme.errorContainer
            )
        }
    }
}

@Composable
fun LeyendaItem(label: String, cantidad: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = cantidad.toString(),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
