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

    @Query("select * from $TABLE_NAME where recipeComplexSearchHash = :recipeComplexSearchHash")
    suspend fun selectById(recipeComplexSearchHash: String): CachedRecipeComplex?

    @Query("select * from $TABLE_NAME where position between :from and :to")
    fun selectRecipesByPositionsInterval(from: Int, to: Int): Flow<CachedRecipeComplex>
}