package com.lsimanenka.shared.remote.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("users/register")
    suspend fun register(@Body request: RegisterRequest)

    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("users/logout")
    suspend fun logout()
}