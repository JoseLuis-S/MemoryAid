package com.alberti.memoryaid.domain.model

enum class TipoEvento(val nombre: String) {
    MEDICACION("Medicación"),
    ALIMENTACION("Alimentación"),
    CRISIS_CONDUCTA("Crisis / Conducta"),
    ESTADO_ANIMO("Estado de animo"),
    OTROS("Otros"),
    NOTAS_GENERALES("Notas generales")
}