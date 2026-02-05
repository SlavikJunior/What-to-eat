package com.example.whattoeat.di

import android.content.Context
import androidx.room.Room
import com.example.whattoeat.data.database.WhatToEatDatabase
import com.example.whattoeat.data.database.dao.CachedRecipeDao
import com.example.whattoeat.data.database.dao.FavoriteRecipeDao
import com.example.whattoeat.data.database.dao.UsersRecipeDao
import com.example.whattoeat.data.database.repository.FavoriteRecipeRepositoryImpl
import com.example.whattoeat.data.database.repository.UsersRecipeRepositoryImpl
import com.example.whattoeat.data.net.repository.RecipeSearchRepositoryImpl
import com.example.whattoeat.data.net.service.SpoonacularApiService
import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideSpoonacularApiService(): SpoonacularApiService {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(SpoonacularApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeSearchRepository(
        service: SpoonacularApiService,
        cachedRecipeDao: CachedRecipeDao
    ): RecipeSearchRepository =
        RecipeSearchRepositoryImpl(
            apiKey = System.getenv("SPOONACULAR_API_KEY")!!,
            service = service,
            cachedRecipeDao = cachedRecipeDao
        )

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
    fun provideCachedRecipeDao(whatToEatDatabase: WhatToEatDatabase) =
        whatToEatDatabase.cachedRecipeDao()

    @Provides
    @Singleton
    fun provideFavoriteRecipeDao(whatToEatDatabase: WhatToEatDatabase) =
        whatToEatDatabase.favoriteRecipeDao()

    @Provides
    @Singleton
    fun provideUsersRecipeDao(whatToEatDatabase: WhatToEatDatabase) =
        whatToEatDatabase.usersRecipeDao()

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