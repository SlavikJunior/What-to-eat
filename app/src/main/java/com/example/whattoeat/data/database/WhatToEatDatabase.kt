package com.example.whattoeat.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.whattoeat.data.database.dao.RecipeDAO
import com.example.whattoeat.data.database.entity.RecipeEntity

@Database(
    entities = [RecipeEntity::class],
    version = WhatToEatDatabase.WHAT_TO_EAT_DATABASE_VERSION,
    exportSchema = false // TODO: костыль, возможно надо изменить настройки плагина
)
abstract class WhatToEatDatabase: RoomDatabase() {

    abstract fun recipeDao(): RecipeDAO

    companion object {
        const val WHAT_TO_EAT_DATABASE_VERSION = 1
        const val WHAT_TO_EAT_DATABASE_NAME = "what_to_eat_database"
    }
}