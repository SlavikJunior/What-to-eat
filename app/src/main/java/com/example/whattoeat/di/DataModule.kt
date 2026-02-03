package com.example.whattoeat.di

import android.content.Context
import androidx.room.Room
import com.example.whattoeat.data.database.WhatToEatDatabase
import com.example.whattoeat.data.database.dao.RecipeDAO
import com.example.whattoeat.data.database.repository.FavoriteRecipeRepositoryImpl
import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRecipeSearchRepository(): RecipeSearchRepository =
        TODO("Provide feature RecipeSearchRepositoryImpl here")

    @Provides
    @Singleton
    fun provideWhatToEatDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context = context,
            klass = WhatToEatDatabase::class.java,
            name = WhatToEatDatabase.WHAT_TO_EAT_DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideRecipeDAO(whatToEatDatabase: WhatToEatDatabase) =
        whatToEatDatabase.recipeDao()

    @Provides
    @Singleton
    fun provideFavoriteRecipeRepository(recipeDAO: RecipeDAO): FavoriteRecipeRepository =
        FavoriteRecipeRepositoryImpl(recipeDAO = recipeDAO)

    @Provides
    @Singleton
    fun provideUsersRecipeRepository(): UsersRecipeRepository =
        TODO("Provide feature UsersRecipeRepositoryImpl here")

}