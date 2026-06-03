package com.example.myapplication.data.repository

import com.example.myapplication.data.AuthResponse
import com.example.myapplication.data.AuthService
import com.example.myapplication.data.LoginRequest
import com.example.myapplication.data.RetrofitClient
import retrofit2.Response

class AuthRepository {
    private val api = RetrofitClient.authService

    suspend fun login(user: String, pass: String): AuthResult {
        return try {
            val response = api.login(LoginRequest(user, pass))

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.access_token != null) {
                    AuthResult.Success(body.access_token)
                } else {
                    AuthResult.Error("Błąd serwera: brak tokena w odpowiedzi")
                }
            } else {
                when (response.code()) {
                    401 -> AuthResult.Error("Nieprawidłowy login lub hasło")
                    else -> AuthResult.Error("Błąd serwera: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            AuthResult.NetworkError
        }
    }
}