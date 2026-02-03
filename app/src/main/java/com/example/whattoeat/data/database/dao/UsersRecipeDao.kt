package com.example.whattoeat.data.database.dao

import androidx.room.Dao
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

    @Query("""
        delete from $TABLE_NAME
        where image = :image
    """)
    fun delete(image: String): Int

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

    @Query("""
        select *
        from $TABLE_NAME
        where title like '%' || :query || '%'
    """)
    suspend fun selectByTitle(query: String): Flow<UsersRecipe>

    @Query("""
        select *
        from $TABLE_NAME 
        where extended_ingredients like '%' || :ingredient1 || '%'
           or extended_ingredients like '%' || :ingredient2 || '%'
           or extended_ingredients like '%' || :ingredient3 || '%'
    """)
    suspend fun selectByMultipleIngredients(
        ingredient1: String,
        ingredient2: String? = null,
        ingredient3: String? = null
    ): Flow<UsersRecipe>
}