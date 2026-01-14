package com.alberti.memoryaid.data.local

import com.alberti.memoryaid.domain.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {

    private val _rolActual = MutableStateFlow(UserRole.USER)
    val rolActual: StateFlow<UserRole> = _rolActual.asStateFlow()

    fun setRole(role: UserRole) {
        _rolActual.value = role
    }

    fun loginComoAdmin(pin: String): Boolean {
        return if (pin == "1234") {
            _rolActual.value = UserRole.ADMIN
            true
        } else {
            false
        }
    }

    fun logout() {
        _rolActual.value = UserRole.USER
    }
}