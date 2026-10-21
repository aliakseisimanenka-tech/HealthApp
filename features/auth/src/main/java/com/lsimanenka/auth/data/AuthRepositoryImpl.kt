package com.lsimanenka.auth.data

import com.lsimanenka.auth.domain.AuthRepository
import com.lsimanenka.shared.database.WorkoutDao
import com.lsimanenka.shared.datastore.TokenManager
import com.lsimanenka.shared.remote.auth.AuthApi
import com.lsimanenka.shared.remote.auth.LoginRequest
import com.lsimanenka.shared.remote.auth.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager,
    private val workoutDao: WorkoutDao
) : AuthRepository {

    private val _currentUserEmail = MutableStateFlow<String?>(null)
    override val currentUserEmail: Flow<String?> = _currentUserEmail.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)
    override val currentUserId: Flow<String?> = _currentUserId.asStateFlow()

    init {
        val cachedUserId = tokenManager.getUserId()
        val cachedToken = tokenManager.getToken()
        val cachedUserEmail = tokenManager.getUserEmail()

        if (!cachedUserId.isNullOrEmpty() && !cachedToken.isNullOrEmpty() && !cachedUserEmail.isNullOrEmpty()) {
            _currentUserId.value = cachedUserId
            _currentUserEmail.value = cachedUserEmail
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApi.login(LoginRequest(login = email, password = password))

                tokenManager.saveAuthData(token = response.userToken, userId = response.objectId, userEmail = response.email)

                workoutDao.assignAnonymousWorkoutsToUser(response.objectId)

                _currentUserEmail.value = response.email
                _currentUserId.value = response.objectId

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun register(email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                authApi.register(RegisterRequest(email = email, password = password))
                login(email, password)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                workoutDao.clearAllUserWorkouts()

                authApi.logout()

                tokenManager.clear()
                _currentUserEmail.value = null
                _currentUserId.value = null

                Result.success(Unit)
            } catch (e: Exception) {
                workoutDao.clearAllUserWorkouts()
                tokenManager.clear()
                _currentUserEmail.value = null
                _currentUserId.value = null
                Result.failure(e)
            }
        }
    }
}