package com.example.myapplication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    indices = [Index(value = ["Nazwa"], unique = true)]
)
data class Kawa(
    @PrimaryKey(autoGenerate = true) val id: Int =0,
    val Kraj: String? = null,
    val Nazwa: String,
    val Palenie: String? = null,
    val Gatunek: String? = null,
    val Opis: String? = null
)


@Entity (foreignKeys = [ForeignKey(entity = Kawa::class,
                                        parentColumns = ["id"],
                                        childColumns = ["idKawa"],
                                        onDelete = CASCADE)])
data class Wypicie(
    @PrimaryKey(autoGenerate = true) val id: Int =0,
    val data: LocalDate? = null,
    val gramy: Double? = null,
    val rozmiarMlynka: Int? = null,
    val ocena: Int? = null,
    val idKawa: Int? = null
)