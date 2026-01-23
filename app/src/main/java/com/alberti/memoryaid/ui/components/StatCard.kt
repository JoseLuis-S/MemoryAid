package com.alberti.memoryaid.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Tarjeta de información estadística diseñada para el panel de administración.
 * * Proporciona una visualización rápida de métricas clave mediante una jerarquía
 * tipográfica clara que enfatiza el valor numérico sobre la etiqueta.
 * * @param titulo Texto identificador de la métrica (se renderiza en mayúsculas).
 * @param valor Dato principal a destacar, generalmente numérico.
 * @param subtitulo Información contextual opcional (ej: tendencia o comparativa porcentual).
 * @param containerColor Color de fondo del contenedor, por defecto usa el esquema secundario.
 * @param contentColor Color para los textos, adaptado automáticamente al fondo por defecto.
 * @param modifier Modificador para ajustar el layout (pesos, dimensiones, etc.).
 */
@Composable
fun StatCard(
    titulo: String,
    valor: String,
    subtitulo: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = titulo.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = contentColor
            )
            subtitulo?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}
