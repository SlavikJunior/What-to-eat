package com.example.whattoeat.di

import android.content.Context
import androidx.room.Room
import com.example.whattoeat.BuildConfig
import com.example.whattoeat.data.database.WhatToEatDatabase
import com.example.whattoeat.data.database.dao.CachedRecipeDao
import com.example.whattoeat.data.database.dao.FavoriteRecipeDao
import com.example.whattoeat.data.database.dao.UsersRecipeDao
import com.example.whattoeat.data.database.repository.FavoriteRecipeRepositoryImpl
import com.example.whattoeat.data.database.repository.UsersRecipeRepositoryImpl
import com.example.whattoeat.data.net.adapter.ResultCallAdapterFactory
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
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cacheFile = File(context.cacheDir, "http-cache")
        val cache = Cache(
            directory = cacheFile,
            maxSize = (50 * 1024 * 1024).toLong() // 50 MB
        )

        val cacheInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())

            val cacheControl = CacheControl.Builder()
                .maxAge(7, TimeUnit.DAYS)
                .maxStale(7, TimeUnit.DAYS)
                .build()


            response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addNetworkInterceptor(cacheInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .cache(cache)
            .build()

    }

    @Provides
    @Singleton
    fun provideJson() =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
            explicitNulls = false
        }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideSpoonacularApiService(retrofit: Retrofit): SpoonacularApiService {
        return retrofit.create(SpoonacularApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeSearchRepository(
        service: SpoonacularApiService,
        cachedRecipeDao: CachedRecipeDao
    ): RecipeSearchRepository {
        var apiKey: String = BuildConfig.SPOONACULAR_API_KEY
        if (apiKey.isEmpty() || apiKey == "\"\"")
            apiKey = System.getenv("SPOONACULAR_API_KEY")
                ?: throw IllegalStateException("API key not found. Add SPOONACULAR_API_KEY to local.properties")

        return RecipeSearchRepositoryImpl(
            apiKey = apiKey,
            service = service,
            cachedRecipeDao = cachedRecipeDao
        )
    }


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
