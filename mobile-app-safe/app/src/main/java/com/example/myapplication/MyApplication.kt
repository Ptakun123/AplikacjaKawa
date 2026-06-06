package com.example.myapplication

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.RetrofitClient
import com.example.myapplication.data.TokenManager
import com.example.myapplication.data.roomdatabase.AppDatabase
import com.example.myapplication.ui.kawaview.KawaViewModelFactory
import com.example.myapplication.ui.wypicieview.WypicieViewModelFactory
import com.example.myapplication.ui.loginview.LoginViewModelFactory
import com.example.myapplication.data.repository.AuthRepository

class MyApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}

class AppContainer(private val context: Context) {
    private val db by lazy{
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "kawa-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    val authService by lazy { RetrofitClient.authService }
    val tokenManager by lazy { TokenManager(context) }
    val authRepository by lazy { AuthRepository(authService, tokenManager) }

    val loginViewModelFactory by lazy { LoginViewModelFactory(authRepository) }
    val wypicieViewModelFactory by lazy { WypicieViewModelFactory(db) }
    val kawaViewModelFactory by lazy { KawaViewModelFactory(db) }
}