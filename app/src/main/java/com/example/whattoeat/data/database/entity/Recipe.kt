package com.example.whattoeat.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(index = true)
    val name: String,
    val photo: String? = null,
    @Embedded
    val nutritionalValue: NutritionalValue
)

data class NutritionalValue(
    val proteins: Int? = null,
    val fats: Int? = null,
    val carbohydrates: Int? = null,
    val calories: Int? = null
)