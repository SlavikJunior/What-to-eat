package com.example.whattoeat.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.whattoeat.data.database.entity.RecipeEntity

@Dao
interface RecipeDAO {

    @Query("""
        SELECT * FROM recipes 
        WHERE title = :title 
        AND description = :description
    """)
    suspend fun isStored(title: String, description: String): RecipeEntity?

    @Insert
    suspend fun insert(recipe: RecipeEntity): Boolean

    @Delete
    suspend fun delete(recipe: RecipeEntity): Boolean
}