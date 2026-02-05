package com.example.whattoeat.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.whattoeat.data.database.entity.CachedRecipeComplex
import com.example.whattoeat.data.database.entity.CachedRecipeComplex.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedRecipeComplexDao {

    @Upsert(entity = CachedRecipeComplex::class)
    suspend fun upsert(recipe: CachedRecipeComplex): Long

    @Delete(entity = CachedRecipeComplex::class)
    suspend fun delete(recipe: CachedRecipeComplex): Int

    @Query("select * from $TABLE_NAME where recipe_complex_search_hash = :recipeComplexSearchHash")
    suspend fun selectByHash(recipeComplexSearchHash: String): List<CachedRecipeComplex>
}