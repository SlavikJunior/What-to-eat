package com.example.whattoeat.di

import com.example.whattoeat.data.database.dao.FavoriteRecipeDao
import com.example.whattoeat.data.database.dao.UsersRecipeDao
import com.example.whattoeat.data.database.repository.FavoriteRecipeRepositoryImpl
import com.example.whattoeat.data.database.repository.UsersRecipeRepositoryImpl
import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideFavoriteRecipeRepository(favoriteRecipeDao: FavoriteRecipeDao): FavoriteRecipeRepository =
        FavoriteRecipeRepositoryImpl(
            favoriteRecipeDao = favoriteRecipeDao
        )

    @Provides
    @Singleton
    fun provideUsersRecipeRepository(usersRecipeDao: UsersRecipeDao): UsersRecipeRepository =
        UsersRecipeRepositoryImpl(
            usersRecipeDao = usersRecipeDao
        )
}