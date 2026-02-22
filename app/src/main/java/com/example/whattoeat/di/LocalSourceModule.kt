package com.example.whattoeat.di

import android.content.Context
import androidx.room.Room
import com.example.whattoeat.data.database.Migration_1_2
import com.example.whattoeat.data.database.WhatToEatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalSourceModule {

    @Provides
    @Singleton
    fun provideWhatToEatDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context = context,
            klass = WhatToEatDatabase::class.java,
            name = WhatToEatDatabase.WHAT_TO_EAT_DATABASE_NAME
        )
            .addMigrations(Migration_1_2)
            .build()

    @Provides
    @Singleton
    fun provideFavoriteRecipeDao(whatToEatDatabase: WhatToEatDatabase) =
        whatToEatDatabase.favoriteRecipeDao()

    @Provides
    @Singleton
    fun provideUsersRecipeDao(whatToEatDatabase: WhatToEatDatabase) =
        whatToEatDatabase.usersRecipeDao()

}