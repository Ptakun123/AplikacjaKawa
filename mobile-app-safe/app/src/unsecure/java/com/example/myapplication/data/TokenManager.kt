package com.example.myapplication.data

import android.content.Context
import android.content.SharedPreferences

class TokenManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("unsecure_prefs", Context.MODE_PRIVATE)

    suspend fun saveToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }
    fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }
}