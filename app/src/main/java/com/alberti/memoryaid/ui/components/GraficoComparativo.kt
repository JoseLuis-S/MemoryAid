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
            text = "Distribución Medicación vs Crisis",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(if (pctMedicinas > 0f) pctMedicinas else 0.001f)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(if (pctCrisis > 0f) pctCrisis else 0.001f)
                    .background(MaterialTheme.colorScheme.error)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LeyendaItem(label = "Medicinas ($medicinas)", color = MaterialTheme.colorScheme.primary)
            LeyendaItem(label = "Crisis ($crisis)", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun LeyendaItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}