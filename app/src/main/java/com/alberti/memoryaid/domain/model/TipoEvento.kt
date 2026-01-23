package com.alberti.memoryaid.domain.model

/**
 * Define las categorías permitidas para los eventos de memoria en la aplicación.
 * * Este enumerado se utiliza para tipificar los registros, facilitar el filtrado
 * en la capa de datos y determinar el comportamiento visual/notificaciones
 * en la capa de interfaz de usuario.
 *
 * @property nombre Representación textual legible para el usuario (String amigable).
 */
enum class TipoEvento(val nombre: String) {
    /** Registro de toma o administración de fármacos. */
    MEDICACION("Medicación"),

    /** Eventos relacionados con la ingesta de alimentos o hidratación. */
    ALIMENTACION("Alimentación"),

    /** Reportes de incidentes conductuales o situaciones críticas. */
    CRISIS_CONDUCTA("Crisis / Conducta"),

    /** Seguimiento del bienestar emocional del usuario. */
    ESTADO_ANIMO("Estado de animo"),

    /** Categoría para eventos que no encajan en las definiciones anteriores. */
    OTROS("Otros"),

    /** Anotaciones libres o información suplementaria. */
    NOTAS_GENERALES("Notas generales")
}
