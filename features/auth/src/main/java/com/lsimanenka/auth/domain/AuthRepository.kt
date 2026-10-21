package com.lsimanenka.auth.domain

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserEmail: Flow<String?>
    val currentUserId: Flow<String?>

    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
}