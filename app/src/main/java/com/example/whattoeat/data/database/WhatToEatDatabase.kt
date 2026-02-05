package com.example.whattoeat.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.whattoeat.data.database.dao.*
import com.example.whattoeat.data.database.entity.*

@Database(
    entities = [
        CachedRecipeComplex::class,
        UsersRecipe::class,
        FavoriteRecipe::class
    ],
    version = WhatToEatDatabase.WHAT_TO_EAT_DATABASE_VERSION,
    exportSchema = false // TODO: костыль, возможно надо изменить настройки плагина
)
@TypeConverters(com.example.whattoeat.data.database.TypeConverters::class)
abstract class WhatToEatDatabase : RoomDatabase() {

    abstract fun cachedRecipeDao(): CachedRecipeComplexDao
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao
    abstract fun usersRecipeDao(): UsersRecipeDao

    companion object {
        const val WHAT_TO_EAT_DATABASE_VERSION = 1
        const val WHAT_TO_EAT_DATABASE_NAME = "what_to_eat_database"
    }
}