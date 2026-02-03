package com.example.whattoeat.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.whattoeat.data.database.entity.CachedRecipe.Companion.TABLE_NAME
import com.example.whattoeat.data.database.entity.UsersRecipe
import com.example.whattoeat.domain.domain_entities.common.RecipeByUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersRecipeDao {

    @Insert(entity = UsersRecipe::class)
    fun insert(recipeByUser: RecipeByUser): Long

    @Delete(entity = UsersRecipe::class)
    fun delete(id: Int): Int

    @Query("""
        select *
        from $TABLE_NAME
        where id = :id
    """)
    suspend fun selectById(id: Int): UsersRecipe

    @Query("""
        select *
        from $TABLE_NAME
    """)
    suspend fun selectAll(): Flow<UsersRecipe>
}