package com.example.myapplication

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Kawa::class, Wypicie::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun kawaDao(): KawaDao
    abstract fun wypicieDao(): WypicieDao
}