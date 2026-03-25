package com.example.myapplication

import android.app.Application
import androidx.room.Room

class MyApplication: Application(){

    val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "kawa-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}