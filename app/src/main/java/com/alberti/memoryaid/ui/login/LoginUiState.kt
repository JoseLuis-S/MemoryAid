package com.alberti.memoryaid.ui.login

/**
 * Representa el estado atómico y reactivo de la pantalla de Login.
 *
 * Sigue los principios de **Unidirectional Data Flow (UDF)**, encapsulando todas las
 * variables que determinan la representación visual de la pantalla en un único objeto inmutable.
 *
 * @property mostrarDialogoPin Indica si el diálogo de desafío/configuración de PIN debe estar visible.
 * @property esPrimeraVezAdmin Flag que determina si el usuario está configurando un PIN por
 * primera vez o si está intentando autenticarse como administrador.
 * @property pinInput Buffer que almacena el texto ingresado actualmente en el campo de PIN.
 * @property errorPin Mensaje de error descriptivo en caso de que la validación del PIN falle.
 * Es `null` si no hay errores activos.
 * @property loginExitoso Disparador (trigger) para la navegación. Cuando cambia a `true`,
 * la UI debe reaccionar navegando hacia el panel principal.
 */
data class LoginUiState(
    val mostrarDialogoPin: Boolean = false,
    val esPrimeraVezAdmin: Boolean = false,
    val pinInput: String = "",
    val errorPin: String? = null,
    val loginExitoso: Boolean = false
)
