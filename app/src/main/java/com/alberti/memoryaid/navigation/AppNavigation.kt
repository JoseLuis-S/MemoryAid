package com.alberti.memoryaid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alberti.memoryaid.navigation.Rutas.RutaHome
import com.alberti.memoryaid.navigation.Rutas.RutaRegistro
import com.alberti.memoryaid.navigation.Rutas.RutaLogin
import com.alberti.memoryaid.navigation.Rutas.RutaAdmin
import com.alberti.memoryaid.ui.home.HomeScreen
import com.alberti.memoryaid.ui.registro.RegistroScreen
import com.alberti.memoryaid.ui.login.LoginScreen
import com.alberti.memoryaid.ui.admin.AdminScreen

/**
 * Orquestador principal de la navegación de la interfaz de usuario.
 *
 * Define el grafo de navegación de la aplicación utilizando **Jetpack Navigation Compose**.
 * Gestiona las transiciones entre pantallas y la lógica de limpieza de la pila de retroceso
 * (*backstack*) para flujos de autenticación y navegación jerárquica.
 *
 * @param navController Controlador de navegación que gestiona el estado de las pantallas.
 * Por defecto utiliza [rememberNavController].
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = RutaLogin
    ) {
        /**
         * Pantalla de acceso inicial.
         * Al autenticarse con éxito, navega a Home eliminando la ruta de Login del historial
         * para evitar que el usuario regrese con el botón "Atrás".
         */
        composable<RutaLogin> {
            LoginScreen(
                onNavegarAHome = {
                    navController.navigate(RutaHome) {
                        popUpTo(RutaLogin) { inclusive = true }
                    }
                }
            )
        }

        /**
         * Pantalla principal con el historial y resumen de eventos.
         * Permite la navegación hacia la creación de nuevos eventos, edición de existentes
         * mediante paso de parámetros (*type-safe*) y acceso al panel de administración.
         */
        composable<RutaHome> {
            HomeScreen(
                alNavegarARegistro = {
                    navController.navigate(RutaRegistro())
                },
                alEditarEvento = { id ->
                    navController.navigate(RutaRegistro(eventoId = id))
                },
                alNavegarAAdmin = {
                    navController.navigate(RutaAdmin)
                }
            )
        }

        /**
         * Panel de administración para visualización de estadísticas y purga de datos.
         */
        composable<RutaAdmin> {
            AdminScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        /**
         * Formulario de registro y edición de eventos.
         * Utiliza una clase de ruta con parámetros para determinar si se está creando
         * un nuevo registro o editando uno previo.
         */
        composable<RutaRegistro> {
            RegistroScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}
