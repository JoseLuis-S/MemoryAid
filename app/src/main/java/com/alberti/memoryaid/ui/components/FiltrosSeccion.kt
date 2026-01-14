package com.alberti.memoryaid.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.alberti.memoryaid.domain.model.TipoEvento

@Composable
fun FiltrosSeccion(
    seleccionado: TipoEvento?,
    alSeleccionar: (TipoEvento?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = seleccionado == null,
                onClick = { alSeleccionar(null) },
                label = { Text("Todos") }
            )
        }
        items(TipoEvento.entries.toTypedArray()) { tipo ->
            FilterChip(
                selected = seleccionado == tipo,
                onClick = { alSeleccionar(tipo) },
                label = { Text(tipo.nombre) }
            )
        }
    }
}
