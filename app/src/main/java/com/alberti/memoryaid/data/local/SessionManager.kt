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

@Singleton
class SessionManager @Inject constructor(
    private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "settings")
    private val PIN_KEY = stringPreferencesKey("admin_pin")

    private val _rolActual = MutableStateFlow(UserRole.USER)
    val rolActual = _rolActual.asStateFlow()

    fun obtenerPin(): Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[PIN_KEY] }

    suspend fun guardarPin(nuevoPin: String) {
        context.dataStore.edit { preferences ->
            preferences[PIN_KEY] = nuevoPin
        }
        _rolActual.value = UserRole.ADMIN
    }

    suspend fun loginComoAdmin(pinIngresado: String): Boolean {
        val pinGuardado = obtenerPin().first()
        return if (pinGuardado == pinIngresado) {
            _rolActual.value = UserRole.ADMIN
            true
        } else false
    }

    fun setRole(role: UserRole) { _rolActual.value = role }

    fun logout() {
        _rolActual.value = UserRole.USER
    }
}