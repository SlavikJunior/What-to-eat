package com.example.whattoeat.data.database

import androidx.room.Database
import androidx.room.TypeConverters
import com.example.whattoeat.data.database.converter.Converter
import com.example.whattoeat.data.database.entity.FavoriteRecipe
import com.example.whattoeat.data.database.entity.Recipe
import com.example.whattoeat.data.database.entity.User

@Database(
    entities = [User::class, Recipe::class, FavoriteRecipe::class],
    version = WhatToEatDatabase.WHAT_TO_EAT_DATABASE_VERSION
)
@TypeConverters(Converter::class)
abstract class WhatToEatDatabase {

    companion object {
        const val WHAT_TO_EAT_DATABASE_VERSION = 1
    }
}