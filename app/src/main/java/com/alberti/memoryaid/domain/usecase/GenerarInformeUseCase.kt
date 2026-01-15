package com.alberti.memoryaid.domain.usecase

import com.alberti.memoryaid.domain.repository.RepositorioMemoria
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GenerarInformeUseCase @Inject constructor(
    private val repositorio: RepositorioMemoria
) {
    suspend operator fun invoke(): String {
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
                sb.append("🔹 [${sdf.format(Date(evento.fechaHora))}] ${evento.tipo?.nombre}\n")
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
