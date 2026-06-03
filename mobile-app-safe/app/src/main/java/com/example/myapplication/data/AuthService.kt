package com.example.myapplication.data
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

interface AuthService {
    @POST("api/auth/register")
    suspend fun register(
        @Body request: LoginRequest
    ): Response<Unit> // Unit, bo rejestracja zwraca tylko status 201 lub błąd

    // Logowanie - zwraca token JWT
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>
}