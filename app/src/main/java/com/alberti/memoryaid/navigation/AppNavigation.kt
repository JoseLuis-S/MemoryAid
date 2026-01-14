package com.alberti.memoryaid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alberti.memoryaid.navigation.Rutas.RutaHome
import com.alberti.memoryaid.navigation.Rutas.RutaRegistro
import com.alberti.memoryaid.ui.home.HomeScreen
import com.alberti.memoryaid.ui.registro.RegistroScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = RutaHome
    ) {
        composable<RutaHome> {
            HomeScreen(
                alNavegarARegistro = {
                    navController.navigate(RutaRegistro)
                }
            )
        }

        composable<RutaRegistro> {
            RegistroScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}