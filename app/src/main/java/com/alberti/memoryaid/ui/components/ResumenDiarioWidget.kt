package com.alberti.memoryaid.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.R
import com.airbnb.lottie.compose.*

@Composable
fun ResumenDiarioWidget(
    resumen: Map<TipoEvento, Int>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Actividad de Hoy",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (resumen.isNotEmpty()) {
                    val total = resumen.values.sum()
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ) {
                        Text(
                            text = "$total eventos",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (resumen.isEmpty()) {
                EmptyStateVisual()
            } else {
                GraficoBarrasSimple(resumen)

                Spacer(modifier = Modifier.height(24.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(resumen.toList()) { (tipo, cuenta) ->
                        IndicadorEstadistico(tipo = tipo, cuenta = cuenta)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateVisual() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_state))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = "Todo tranquilo por ahora",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun GraficoBarrasSimple(resumen: Map<TipoEvento, Int>) {
    val maxValor = resumen.values.maxOrNull() ?: 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        resumen.forEach { (tipo, cuenta) ->
            val altoProporcional = (cuenta.toFloat() / maxValor.toFloat())
            val animado by animateFloatAsState(
                targetValue = altoProporcional,
                animationSpec = tween(durationMillis = 1000),
                label = "anim_bar"
            )

            val colorBarra = when (tipo) {
                TipoEvento.CRISIS_CONDUCTA -> MaterialTheme.colorScheme.error
                TipoEvento.MEDICACION -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.primary
            }

            Box(
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight(animado)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(colorBarra)
            )
        }
    }
}

@Composable
private fun IndicadorEstadistico(tipo: TipoEvento, cuenta: Int) {
    val (bgColor, contentColor) = when (tipo) {
        TipoEvento.CRISIS_CONDUCTA -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        TipoEvento.MEDICACION -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(contentColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = cuenta.toString(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = bgColor
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = tipo.nombre,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}
