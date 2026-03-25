package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface KawaDao {
    @Insert
    fun insertAll(kawy: List<Kawa>)

    @Delete
    fun delete(kawa: Kawa)

    @Query("SELECT * FROM kawa")
    fun getAll(): Flow<List<Kawa>>

    @Query("SELECT COUNT(*) FROM kawa WHERE Nazwa = :nazwa")
    fun countNazwa(nazwa: String): Int

}


@Dao
interface WypicieDao {
    @Insert
    fun insertAll(wypicia: List<Wypicie>)

    @Delete
    fun delete(wypicie: Wypicie)

    @Query("SELECT * FROM wypicie")
    fun getAll(): List<Wypicie>

    @Query("SELECT id FROM kawa WHERE Nazwa = :nazwa")
    fun getKawaId(nazwa: String): Int
}