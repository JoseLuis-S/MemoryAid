package com.alberti.memoryaid.domain.model

/**
 * Define los niveles de acceso y privilegios dentro de la aplicación.
 * * Se utiliza para implementar el control de acceso basado en roles (RBAC),
 * permitiendo que la interfaz de usuario y los casos de uso adapten su
 * comportamiento según los permisos del usuario actual.
 */
enum class UserRole {
    /**
     * Rol estándar con acceso a funciones básicas de registro y consulta de eventos personales.
     */
    USER,

    /**
     * Rol con privilegios elevados, permitiendo el acceso a estadísticas avanzadas,
     * configuraciones globales y gestión de la seguridad (PIN).
     */
    ADMIN
}
