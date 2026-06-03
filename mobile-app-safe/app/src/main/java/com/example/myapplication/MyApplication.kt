package com.example.myapplication

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.roomdatabase.AppDatabase
import com.example.myapplication.ui.kawaview.KawaViewModelFactory
import com.example.myapplication.ui.wypicieview.WypicieViewModelFactory
import com.example.myapplication.ui.loginview.LoginViewModelFactory
import com.example.myapplication.data.repository.AuthRepository

class MyApplication : Application() {
    // Repozytorium żyje tak długo jak proces aplikacji
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
    val authRepository by lazy { AuthRepository() }

    val loginViewModelFactory by lazy { LoginViewModelFactory(authRepository) }
    val wypicieViewModelFactory by lazy { WypicieViewModelFactory(db) }
    val kawaViewModelFactory by lazy { KawaViewModelFactory(db) }
}