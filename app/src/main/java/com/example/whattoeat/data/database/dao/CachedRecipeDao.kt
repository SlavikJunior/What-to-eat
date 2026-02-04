package com.example.whattoeat.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.whattoeat.data.database.entity.CachedRecipe
import com.example.whattoeat.data.database.entity.CachedRecipe.Companion.TABLE_NAME
import com.example.whattoeat.domain.domain_entities.common.RecipeFullInformation
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedRecipeDao {

    @Upsert(entity = CachedRecipe::class)
    suspend fun upsert(recipe: RecipeFullInformation): Long

    @Delete(entity = CachedRecipe::class)
    suspend fun delete(id: Int): Int

    @Query("""
        select *
        from $TABLE_NAME
        where id = :id
    """)
    suspend fun selectById(id: Int): CachedRecipe?

    @Query("""
        select *
        from $TABLE_NAME
    """)
    suspend fun selectAll(): Flow<CachedRecipe>
}