package com.example.whattoeat.data.database

import androidx.room.Database
import com.example.whattoeat.data.database.dao.RecipeDAO
import com.example.whattoeat.data.database.entity.RecipeEntity

@Database(
    entities = [RecipeEntity::class],
    version = WhatToEatDatabase.WHAT_TO_EAT_DATABASE_VERSION
)
abstract class WhatToEatDatabase {

    abstract fun recipeDao(): RecipeDAO

    companion object {
        const val WHAT_TO_EAT_DATABASE_VERSION = 1
    }
}