package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Caso de uso encargado de la exportación de datos en formato de texto legible.
 * * Este componente transforma la lista de eventos persistidos en un informe clínico
 * estructurado, ideal para ser compartido por correo electrónico, aplicaciones de
 * mensajería o para impresión.
 *
 * @property repositorio Fuente de verdad de los eventos de memoria.
 */
class GenerarInformeUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    /**
     * Genera un informe textual con todos los eventos registrados hasta el momento.
     * * El método realiza una lectura única (one-shot) del flujo de datos del repositorio,
     * itera sobre cada evento y aplica un formato cronológico.
     *
     * @return Una [String] con el informe clínico formateado. Si no hay eventos,
     * devuelve un mensaje indicando la ausencia de registros.
     */
    suspend operator fun invoke(): String {
        // Consumimos el primer valor emitido por el Flow para obtener una instantánea actual
        val eventos = repositorio.obtenerTodosLosEventos().first()
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val sb = StringBuilder()

        sb.append("📋 INFORME CLÍNICO - MEMORYAID\n")
        sb.append("Fecha de generación: ${sdf.format(Date())}\n")
        sb.append("------------------------------------------\n\n")

        if (eventos.isEmpty()) {
            sb.append("No hay registros en el historial.")
        } else {
            eventos.forEach { evento ->
                val fechaFormateada = sdf.format(Date(evento.fechaHora))
                val tipoNombre = evento.tipo?.nombre ?: "Sin categoría"

                sb.append("🔹 [$fechaFormateada] $tipoNombre\n")
                sb.append("Título: ${evento.titulo}\n")

                if (evento.descripcion.isNotBlank()) {
                    sb.append("Notas: ${evento.descripcion}\n")
                }
                sb.append("\n")
            }
        }

        return sb.toString()
    }
}
