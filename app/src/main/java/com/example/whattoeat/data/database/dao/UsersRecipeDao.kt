package com.example.whattoeat.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.whattoeat.data.database.entity.UsersRecipe
import com.example.whattoeat.data.database.entity.UsersRecipe.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersRecipeDao {

    @Insert(entity = UsersRecipe::class)
    fun insert(recipeByUser: UsersRecipe): Long

    @Query("""
        delete from $TABLE_NAME
        where title = :title
    """)
    fun delete(title: String): Int

    @Query("""
        select *
        from $TABLE_NAME
    """)
    fun selectAll(): Flow<List<UsersRecipe>>?
}