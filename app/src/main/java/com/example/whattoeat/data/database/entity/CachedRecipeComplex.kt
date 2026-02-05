package com.example.whattoeat.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.whattoeat.data.database.entity.CachedRecipeComplex.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [Index("position")],
    primaryKeys = ["recipe_complex_search_hash", "position"]
)
data class CachedRecipeComplex(
    @ColumnInfo("recipe_complex_search_hash")
    val recipeComplexSearchHash: String, // хэш от запроса, не учитывая offset и number и сортировку???
    val position: Int,
    @ColumnInfo(name = "recipe_complex_body")
    val recipeComplexBody: String
) {

    companion object {
        const val TABLE_NAME = "cached_recipes_complex"
    }
}