package com.example.whattoeat.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.whattoeat.data.database.entity.FavoriteRecipe.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME,)
data class FavoriteRecipe(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val TABLE_NAME = "favorite_recipes"
    }
}