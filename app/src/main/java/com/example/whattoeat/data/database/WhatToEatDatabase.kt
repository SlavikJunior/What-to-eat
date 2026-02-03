package com.example.whattoeat.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.whattoeat.data.database.dao.*
import com.example.whattoeat.data.database.entity.*

@Database(
    entities = [
        CachedRecipe::class,
        UsersRecipe::class,
        FavoriteRecipe::class
    ],
    version = WhatToEatDatabase.WHAT_TO_EAT_DATABASE_VERSION,
    exportSchema = false // TODO: костыль, возможно надо изменить настройки плагина
)
abstract class WhatToEatDatabase : RoomDatabase() {

    abstract fun cachedRecipeDao(): CachedRecipeDao
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao
    abstract fun usersRecipeDao(): UsersRecipeDao

    companion object {
        const val WHAT_TO_EAT_DATABASE_VERSION = 1
        const val WHAT_TO_EAT_DATABASE_NAME = "what_to_eat_database"
    }
}