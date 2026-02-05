package com.example.whattoeat.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.whattoeat.data.database.entity.FavoriteRecipe.Companion.TABLE_NAME
import com.example.whattoeat.data.database.entity.FavoriteRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRecipeDao {

    @Insert(entity = FavoriteRecipe::class)
    suspend fun insert(recipe: FavoriteRecipe): Long

    @Delete(entity = FavoriteRecipe::class)
    suspend fun delete(recipe: FavoriteRecipe): Int

    @Query("""
        select *
        from $TABLE_NAME
        where id = :id
    """)
    suspend fun selectById(id: Int): FavoriteRecipe?

    @Query("""
        select *
        from $TABLE_NAME
    """)
    fun selectAll(): Flow<FavoriteRecipe>
}