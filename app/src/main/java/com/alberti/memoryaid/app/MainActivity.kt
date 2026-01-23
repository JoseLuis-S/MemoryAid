package com.alberti.memoryaid.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alberti.memoryaid.data.worker.NotificacionScheduler
import com.alberti.memoryaid.navigation.AppNavigation
import com.alberti.memoryaid.ui.theme.MemoryAidTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Punto de entrada principal de la aplicación.
 * * Esta Activity actúa como el host para la navegación de Jetpack Compose y se encarga de:
 * 1. Inicializar la inyección de dependencias mediante [AndroidEntryPoint].
 * 2. Gestionar permisos críticos en tiempo de ejecución (Notificaciones).
 * 3. Configurar tareas globales del sistema como el resumen diario.
 * 4. Establecer el tema visual y la estructura de navegación.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Inicializa la actividad, configura el modo edge-to-edge y establece el contenido UI.
     * * Se ejecutan las comprobaciones de seguridad y la programación de tareas de fondo
     * antes de inflar la jerarquía de Compose.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificación de permisos para notificaciones push (Android 13+)
        checkNotificationPermission()

        // Registro de la tarea periódica de resumen diario
        NotificacionScheduler.programarResumenDiario(this)

        // Configuración de visualización inmersiva
        enableEdgeToEdge()

        setContent {
            MemoryAidTheme {
                // Punto de entrada del grafo de navegación
                AppNavigation()
            }
        }
    }

    /**
     * Verifica y solicita el permiso de notificaciones según los requerimientos de la API.
     * * A partir de Android 13 (Tiramisu / API 33), es obligatorio solicitar explícitamente
     * el permiso [Manifest.permission.POST_NOTIFICATIONS].
     */
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101 // Request Code para identificación en el callback
                )
            }
        }
    }
}
