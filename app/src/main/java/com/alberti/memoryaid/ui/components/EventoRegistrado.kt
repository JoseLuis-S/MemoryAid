package com.alberti.memoryaid.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alberti.memoryaid.R
import com.airbnb.lottie.compose.*

/**
 * Pantalla de feedback visual y háptico para confirmar el éxito de un registro.
 *
 * Este componente superpone una capa informativa que incluye:
 * 1. **Animación Lottie:** Un check de éxito visual dinámico.
 * 2. **Feedback Háptico:** Ejecuta un patrón de vibración personalizado mediante [Vibrator]
 * adaptándose a diferentes niveles de API de Android (específicamente manejando
 * [VibratorManager] en API 31+).
 * 3. **Mensajería:** Textos de confirmación claros para el usuario.
 *
 * Se recomienda mostrar este componente mediante un cambio de estado temporal en la UI
 * tras una inserción exitosa en la base de datos.
 */
@Composable
fun EventoRegistrado() {
    val context = LocalContext.current
    // Preparación de la composición Lottie desde recursos raw
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success_check))

    /**
     * Efecto secundario disparado al montar el componente.
     * Se encarga de la gestión del hardware de vibración respetando la evolución de las APIs de Android.
     */
    LaunchedEffect(Unit) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+ (API 31) utiliza VibratorManager
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            // Versiones anteriores utilizan el servicio de Vibración directo
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        // Ejecución del patrón de vibración según la versión del SO
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Oreo+ (API 26) permite efectos de forma de onda (Waveform)
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 50, 70, 50), -1))
        } else {
            // Legacy vibration para dispositivos antiguos
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
    }

    // Contenedor principal con fondo semi-transparente para dar contexto de overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LottieAnimation(
                composition = composition,
                iterations = 1, // Ejecuta la animación una sola vez
                modifier = Modifier.size(220.dp)
            )
            Text(
                text = "Registro guardado",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Información almacenada correctamente",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
