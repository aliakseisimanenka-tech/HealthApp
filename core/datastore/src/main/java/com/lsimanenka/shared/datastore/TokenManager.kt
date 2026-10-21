package com.lsimanenka.shared.datastore

import kotlinx.coroutines.flow.Flow

interface TokenManager {
    val userIdFlow: Flow<String?>
    fun getToken(): String?
    fun getUserId(): String?
    fun getUserEmail(): String?
    fun saveAuthData(token: String, userId: String, userEmail: String)
    fun clear()
}