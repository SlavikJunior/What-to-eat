package com.example.whattoeat.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.whattoeat.data.database.entity.UsersRecipe
import com.example.whattoeat.data.database.entity.UsersRecipe.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersRecipeDao {

    @Query(
        """update $TABLE_NAME
        set
            title = :title,
            ready_in_minutes = :readyInMinutes,
            servings = :servings,
            extended_ingredients = :ingredients,
            notes = :notes
            where id = :id
        """
    )
    fun update(
        id: Int,
        title: String,
        readyInMinutes: Int? = null,
        servings: Int? = null,
        ingredients: String?,
        notes: String? = null
    ): Int

    @Insert(entity = UsersRecipe::class)
    fun insert(recipeByUser: UsersRecipe): Long

    @Query(
        """
        delete from $TABLE_NAME
        where title = :title
    """
    )
    fun delete(title: String): Int

    @Query(
        """
        select *
        from $TABLE_NAME
    """
    )
    fun selectAll(): Flow<List<UsersRecipe>>?
}