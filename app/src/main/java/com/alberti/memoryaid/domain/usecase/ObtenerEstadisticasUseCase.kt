package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.model.EstadisticasAdmin
import com.alberti.memoryaid.domain.model.TipoEvento
import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import jakarta.inject.Inject

class ObtenerEstadisticasUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    suspend operator fun invoke(): EstadisticasAdmin {
        val ahora = System.currentTimeMillis()
        val haceUnaSemana = ahora - (7 * 24 * 60 * 60 * 1000)
        val haceDosSemanas = haceUnaSemana - (7 * 24 * 60 * 60 * 1000)

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

    private fun calcularTendencia(actual: Int, previa: Int): String {
        if (previa == 0) return "Sin datos previos"
        val diff = ((actual - previa).toFloat() / previa) * 100
        val simbolo = if (diff >= 0) "↑" else "↓"
        return "$simbolo ${kotlin.math.abs(diff.toInt())}%"
    }
}
