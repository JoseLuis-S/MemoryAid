package com.alberti.memoryaid.navigation

import kotlinx.serialization.Serializable

/**
 * Jerarquía de destinos de navegación de la aplicación.
 * * Define de forma segura (type-safe) todas las rutas posibles mediante el uso de
 * [sealed interface] y objetos/clases marcados con [@Serializable]. Este enfoque
 * permite que el compilador verifique los argumentos de navegación y evita
 * errores por cadenas de texto mal escritas.
 */
sealed interface Rutas {

    /**
     * Pantalla principal de la aplicación.
     * Muestra el historial cronológico de eventos y el acceso a funciones clave.
     */
    @Serializable
    data object RutaHome : Rutas

    /**
     * Pantalla de formulario para el registro o edición de eventos.
     * * @property eventoId Identificador único del evento. Si es `null`, la pantalla
     * se comporta como un formulario de creación; si tiene un valor, se asume
     * modo edición y se precargan los datos correspondientes.
     */
    @Serializable
    data class RutaRegistro(val eventoId: Long? = null) : Rutas

    /**
     * Pantalla de autenticación y acceso al sistema.
     * Es el destino inicial de la aplicación.
     */
    @Serializable
    data object RutaLogin : Rutas

    /**
     * Panel de administración.
     * Acceso restringido para la visualización de analíticas y mantenimiento de la base de datos.
     */
    @Serializable
    data object RutaAdmin : Rutas
}
