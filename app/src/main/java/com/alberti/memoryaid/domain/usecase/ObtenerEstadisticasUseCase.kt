package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EstadisticasAdmin
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject

/**
 * Caso de uso encargado de calcular y consolidar métricas de actividad para el perfil de administrador.
 * * Este componente orquestra múltiples consultas al repositorio para generar una comparativa
 * entre la semana actual y la anterior, permitiendo determinar tendencias críticas en el
 * comportamiento o salud del usuario.
 *
 * @property repositorio Fuente de datos para el conteo de eventos.
 */
class ObtenerEstadisticasUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    /**
     * Calcula las estadísticas de la última semana.
     * * Realiza el cálculo de tres métricas principales: toma de medicación, registros de crisis
     * y volumen total de actividad, además de computar la variación porcentual de crisis
     * respecto a la semana previa.
     *
     * @return Un objeto [EstadisticasAdmin] con los datos calculados.
     */
    suspend operator fun invoke(): EstadisticasAdmin {
        val ahora = System.currentTimeMillis()
        val unaSemanaMs = 7 * 24 * 60 * 60 * 1000L
        val haceUnaSemana = ahora - unaSemanaMs
        val haceDosSemanas = haceUnaSemana - unaSemanaMs

        // Obtención de métricas mediante agregación en el repositorio
        val crisisActual = repositorio.contarPorTipo(TipoEvento.CRISIS_CONDUCTA, haceUnaSemana, ahora)
        val crisisPrevia = repositorio.contarPorTipo(TipoEvento.CRISIS_CONDUCTA, haceDosSemanas, haceUnaSemana)

        val medicinas = repositorio.contarPorTipo(TipoEvento.MEDICACION, haceUnaSemana, ahora)

        val totalRegistros = repositorio.contarTotal(haceUnaSemana, ahora)

        val tendencia = calcularTendencia(crisisActual, crisisPrevia)

        return EstadisticasAdmin(
            medicinasEstaSemana = medicinas,
            crisisEstaSemana = crisisActual,
            notasEstaSemana = totalRegistros,
            tendenciaCrisis = tendencia
        )
    }

    /**
     * Calcula la variación porcentual entre dos periodos.
     * * Aplica la fórmula:
     * $$\Delta\% = \frac{\text{actual} - \text{previa}}{\text{previa}} \times 100$$
     *
     * @param actual Conteo del periodo más reciente.
     * @param previa Conteo del periodo anterior.
     * @return Una representación visual de la tendencia (ej: "↑ 15%" o "↓ 10%").
     */
    private fun calcularTendencia(actual: Int, previa: Int): String {
        if (previa == 0) return "Sin datos previos"
        val diff = ((actual - previa).toFloat() / previa) * 100
        val simbolo = if (diff >= 0) "↑" else "↓"
        return "$simbolo ${kotlin.math.abs(diff.toInt())}%"
    }
}
