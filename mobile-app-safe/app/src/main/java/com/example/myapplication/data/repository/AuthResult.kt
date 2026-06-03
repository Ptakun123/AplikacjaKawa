package com.example.myapplication.data.repository

sealed class AuthResult {
    data class Success(val token: String) : AuthResult()

    data class Error(val message: String) : AuthResult()

    object NetworkError : AuthResult()
}