package com.example.myapplication.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)
@Serializable
data class AuthResponse(
    val access_token: String? = null,
    val error: String? = null
)