package com.alberti.memoryaid.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alberti.memoryaid.domain.model.TipoEvento

/**
 * Fila horizontal de selección de categorías para filtrar el historial de eventos.
 * * Implementa una [LazyRow] que muestra chips interactivos basados en el enumerado [TipoEvento].
 * El componente gestiona un estado de selección único, permitiendo filtrar por una categoría
 * específica o mostrar todos los registros mediante una selección nula.
 *
 * @param seleccionado El [TipoEvento] actualmente activo. Si es `null`, se resalta la opción "Todo el historial".
 * @param alSeleccionar Callback que devuelve el [TipoEvento] seleccionado o `null` si se desea limpiar el filtro.
 */
@Composable
fun FiltrosSeccion(
    seleccionado: TipoEvento?,
    alSeleccionar: (TipoEvento?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.height(64.dp)
    ) {
        // Opción estática para limpiar filtros
        item {
            FilterChip(
                selected = seleccionado == null,
                onClick = { alSeleccionar(null) },
                label = { Text("Todo el historial") },
                shape = RoundedCornerShape(16.dp),
                border = null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        // Opciones dinámicas basadas en el dominio
        items(TipoEvento.entries.toTypedArray()) { tipo ->
            FilterChip(
                selected = seleccionado == tipo,
                onClick = { alSeleccionar(tipo) },
                label = { Text(tipo.nombre) },
                shape = RoundedCornerShape(16.dp),
                border = null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
