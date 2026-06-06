package com.example.myapplication.data.repository

import com.example.myapplication.data.AuthResponse
import com.example.myapplication.data.AuthService
import com.example.myapplication.data.LoginRequest
import com.example.myapplication.data.RetrofitClient
import com.example.myapplication.data.TokenManager
import retrofit2.Response

class AuthRepository (
    private val authApi: AuthService,
    private val tokenManager: TokenManager
){

    suspend fun login(user: String, pass: String): AuthResult {
        return try {
            val response = authApi.login(LoginRequest(user, pass))

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.access_token != null) {
                    tokenManager.saveToken(body.access_token)
                    AuthResult.Success
                } else {
                    AuthResult.Error("Błąd serwera: brak tokena w odpowiedzi")
                }
            } else {
                when (response.code()) {
                    400 -> AuthResult.Error("Brak loginu lub hasła")
                    401 -> AuthResult.Error("Nieprawidłowy login lub hasło")
                    else -> AuthResult.Error("Błąd serwera: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            AuthResult.NetworkError
        }
    }
    suspend fun register(user: String, pass: String): AuthResult {
        return try {
            val response = authApi.register(LoginRequest(user, pass))

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.access_token != null) {
                    tokenManager.saveToken(body.access_token)
                    AuthResult.Success
                } else {
                    AuthResult.Error("Błąd serwera: brak tokena w odpowiedzi")
                }
            } else {
                when (response.code()) {
                    400 -> AuthResult.Error("Brak loginu lub hasła")
                    409 -> AuthResult.Error("Ta nazwa użytkownika już istnieje")
                    else -> AuthResult.Error("Błąd serwera: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            AuthResult.NetworkError
        }
    }
}


