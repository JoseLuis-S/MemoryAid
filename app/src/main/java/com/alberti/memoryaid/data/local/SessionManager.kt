package com.alberti.memoryaid.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.alberti.memoryaid.domain.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestor de sesión y preferencias de usuario.
 * Centraliza la persistencia ligera mediante Jetpack DataStore y gestiona el estado
 * de autenticación (RBAC simple) en memoria.
 *
 * @property context Contexto de la aplicación para el acceso a DataStore.
 */
@Singleton
class SessionManager @Inject constructor(
    private val context: Context
) {
    // Extensión para inicializar DataStore de forma perezosa
    private val Context.dataStore by preferencesDataStore(name = "settings")

    // Claves de acceso para DataStore
    private val PIN_KEY = stringPreferencesKey("admin_pin")
    private val CONTACTO_EMERGENCIA = stringPreferencesKey("contacto_emergencia")

    /**
     * Estado reactivo del rol actual del usuario.
     * Nota: Este estado es volátil y reside en memoria. Se reinicia a [UserRole.USER]
     * al destruir el proceso de la aplicación.
     */
    private val _rolActual = MutableStateFlow(UserRole.USER)
    val rolActual = _rolActual.asStateFlow()

    /**
     * Recupera el PIN de administración almacenado.
     * @return [Flow] que emite el PIN actual o null si no se ha configurado.
     */
    fun obtenerPin(): Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[PIN_KEY] }

    /**
     * Persiste un nuevo PIN y otorga automáticamente privilegios de administrador.
     * @param nuevoPin El nuevo código a guardar.
     */
    suspend fun guardarPin(nuevoPin: String) {
        context.dataStore.edit { preferences ->
            preferences[PIN_KEY] = nuevoPin
        }
        _rolActual.value = UserRole.ADMIN
    }

    /**
     * Valida si el PIN ingresado coincide con el persistido.
     * Si la validación es exitosa, actualiza [rolActual] a [UserRole.ADMIN].
     * @param pinIngresado Código introducido por el usuario.
     * @return `true` si el acceso es concedido, `false` en caso contrario.
     */
    suspend fun loginComoAdmin(pinIngresado: String): Boolean {
        val pinGuardado = obtenerPin().first()
        return if (pinGuardado == pinIngresado) {
            _rolActual.value = UserRole.ADMIN
            true
        } else false
    }

    /**
     * Flow que emite el número de contacto de emergencia guardado.
     */
    val contactoEmergencia: Flow<String?> = context.dataStore.data
        .map { it[CONTACTO_EMERGENCIA] }

    /**
     * Actualiza el número de contacto de emergencia en el almacenamiento persistente.
     * @param numero El número telefónico a guardar.
     */
    suspend fun guardarContactoEmergencia(numero: String) {
        context.dataStore.edit { it[CONTACTO_EMERGENCIA] = numero }
    }

    /**
     * Cambia manualmente el rol del usuario en la sesión actual.
     * @param role Nuevo rol a aplicar.
     */
    fun setRole(role: UserRole) { _rolActual.value = role }

    /**
     * Revoca los privilegios de administrador, devolviendo el rol a [UserRole.USER].
     */
    fun logout() {
        _rolActual.value = UserRole.USER
    }
}
