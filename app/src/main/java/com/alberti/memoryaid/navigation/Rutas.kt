package com.alberti.memoryaid.navigation

import kotlinx.serialization.Serializable

sealed interface Rutas {

    @Serializable
    data object RutaHome : Rutas

    @Serializable
    data object RutaRegistro : Rutas

    @Serializable
    data object RutaLogin : Rutas

    @Serializable
    data object RutaAdmin : Rutas
}