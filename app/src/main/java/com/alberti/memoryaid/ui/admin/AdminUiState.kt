package com.alberti.memoryaid.ui.admin

import com.alberti.memoryaid.domain.model.EstadisticasAdmin

/**
 * Representa el estado atómico de la interfaz de usuario en la pantalla de Administración.
 * * Este objeto es consumido por la composición para reaccionar a cambios en los datos,
 * visibilidad de diálogos y eventos de un solo disparo como la generación de informes.
 *
 * @property estaCargando Indica si hay una operación asíncrona en curso (ej: carga de DB).
 * @property estadisticas Modelo de dominio que contiene las métricas semanales calculadas.
 * @property mostrarDialogoPin Controla la visibilidad del diálogo de configuración/cambio de PIN.
 * @property nuevoPinInput Almacena temporalmente el texto ingresado en el campo de PIN.
 * @property errorValidacion Mensaje de error descriptivo cuando una validación (PIN, Teléfono) falla.
 * @property mostrarDialogoEmergencia Controla la visibilidad del diálogo de contacto de emergencia.
 * @property nuevoEmergenciaInput Almacena temporalmente el número telefónico ingresado.
 * @property mostrarConfirmacionPurga Flag de seguridad para mostrar el aviso de borrado total.
 * @property informeGenerado Contiene el cuerpo del informe clínico listo para ser compartido.
 * Si es null, no hay informe pendiente de procesar.
 * @property necesitaConfiguracion Verdadero si el sistema detecta que es la primera vez
 * que se accede y se requiere establecer un PIN inicial.
 */
data class AdminUiState(
    val estaCargando: Boolean = false,
    val estadisticas: EstadisticasAdmin = EstadisticasAdmin(0, 0, 0, ""),
    val mostrarDialogoPin: Boolean = false,
    val nuevoPinInput: String = "",
    val errorValidacion: String? = null,
    val mostrarDialogoEmergencia: Boolean = false,
    val nuevoEmergenciaInput: String = "",
    val mostrarConfirmacionPurga: Boolean = false,
    val informeGenerado: String? = null,
    val necesitaConfiguracion: Boolean = false
)
