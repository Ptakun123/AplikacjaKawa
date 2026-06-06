package com.example.myapplication.data.repository

sealed class AuthResult {
    data object Success : AuthResult()

    data class Error(val message: String) : AuthResult()

    object NetworkError : AuthResult()
}