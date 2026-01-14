package com.alberti.memoryaid.domain.model

data class EstadisticasAdmin(
    val medicinasEstaSemana: Int = 0,
    val crisisEstaSemana: Int = 0,
    val notasEstaSemana: Int = 0,
    val tendenciaCrisis: String = "Sin datos"
)
