package com.alberti.memoryaid.ui.home

import com.alberti.memoryaid.domain.model.EventoMemoria
import com.alberti.memoryaid.domain.model.TipoEvento

/**
 * Representa el estado atómico y reactivo de la pantalla de Inicio (Home).
 *
 * Sigue el patrón **Unidirectional Data Flow (UDF)**, centralizando todas las variables
 * que afectan a la interfaz de usuario en un único objeto inmutable. Esto facilita la
 * consistencia del estado, las pruebas unitarias y la depuración.
 *
 * @property eventos Lista de eventos filtrados y procesados para mostrar en el historial.
 * @property estaCargando Indica si hay una operación de lectura o proceso en segundo plano activa.
 * @property filtroSeleccionado Categoría actual aplicada al historial. Si es `null`, se muestran todos los tipos.
 * @property mensajeError Texto descriptivo de errores globales (ej. fallo al cargar base de datos).
 * @property mostrarDialogoPin Controla la visibilidad del desafío de seguridad para entrar al área Admin.
 * @property pinInput Buffer temporal que almacena los dígitos del PIN introducidos por el usuario.
 * @property errorPin Mensaje de validación si el PIN introducido es incorrecto o inválido.
 * @property navegarAAdmin Flag que dispara el evento de navegación hacia el panel de administración.
 * @property mostrarDialogoConfigContacto Visibilidad del diálogo para establecer el teléfono de emergencia.
 * @property mostrarDialogoConfigInicial Visibilidad del aviso cuando no se detecta configuración previa del sistema.
 */
data class HomeUiState(
    val eventos: List<EventoMemoria> = emptyList(),
    val estaCargando: Boolean = false,
    val filtroSeleccionado: TipoEvento? = null,
    val mensajeError: String? = null,
    val mostrarDialogoPin: Boolean = false,
    val pinInput: String = "",
    val errorPin: String? = null,
    val navegarAAdmin: Boolean = false,
    val mostrarDialogoConfigContacto: Boolean = false,
    val mostrarDialogoConfigInicial: Boolean = false
)
