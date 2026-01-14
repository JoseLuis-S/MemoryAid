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

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = RutaLogin
    ) {
        composable<RutaLogin> {
            LoginScreen(
                onNavegarAHome = {
                    navController.navigate(RutaHome) {
                        popUpTo(RutaLogin) { inclusive = true }
                    }
                }
            )
        }

        composable<RutaHome> {
            HomeScreen(
                alNavegarARegistro = {
                    navController.navigate(RutaRegistro)
                },
                alNavegarAAdmin = {
                    navController.navigate(RutaAdmin)
                }
            )
        }

        composable<RutaAdmin> {
            AdminScreen(
                onBack = {
                    navController.popBackStack()
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
