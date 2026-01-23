package com.alberti.memoryaid.domain.model

/**
 * Modelo de datos que representa el resumen analítico para el perfil de administrador.
 * * Este objeto agrupa métricas clave recolectadas durante la semana en curso, facilitando
 * la visualización de tendencias y el seguimiento del estado del paciente/usuario.
 *
 * @property medicinasEstaSemana Total de eventos de tipo medicación registrados en los últimos 7 días.
 * @property crisisEstaSemana Cantidad total de reportes de crisis detectados en la semana actual.
 * @property notasEstaSemana Conteo de notas generales o eventos informativos creados recientemente.
 * @property tendenciaCrisis Descripción textual del comportamiento de las crisis (ej: "En aumento", "Estable").
 */
data class EstadisticasAdmin(
    val medicinasEstaSemana: Int = 0,
    val crisisEstaSemana: Int = 0,
    val notasEstaSemana: Int = 0,
    val tendenciaCrisis: String = "Sin datos"
)
