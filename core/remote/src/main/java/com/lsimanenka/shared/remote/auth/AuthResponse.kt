package com.lsimanenka.shared.remote.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("user-token") val userToken: String,
    @SerializedName("objectId") val objectId: String,
    val email: String
)