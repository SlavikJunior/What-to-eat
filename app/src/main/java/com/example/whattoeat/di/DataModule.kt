package com.example.whattoeat.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.whattoeat.BuildConfig
import com.example.whattoeat.data.database.WhatToEatDatabase
import com.example.whattoeat.data.database.dao.CachedRecipeComplexDao
import com.example.whattoeat.data.database.dao.FavoriteRecipeDao
import com.example.whattoeat.data.database.dao.UsersRecipeDao
import com.example.whattoeat.data.database.repository.FavoriteRecipeRepositoryImpl
import com.example.whattoeat.data.database.repository.UsersRecipeRepositoryImpl
import com.example.whattoeat.data.yandex_translate.TranslateApiRepositoryImpl
import com.example.whattoeat.data.net.adapter.ResultCallAdapterFactory
import com.example.whattoeat.data.net.repository.RecipeSearchRepositoryImpl
import com.example.whattoeat.data.net.service.SpoonacularApiService
import com.example.whattoeat.data.yandex_translate.TranslateApiService
import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.domain.repositories.TranslateApiRepository
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
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @SpoonacularOkHttpClient
    @Provides
    @Singleton
    fun provideSpoonacularOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
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

    @SpoonacularJson
    @Provides
    @Singleton
    fun provideJson() =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
            explicitNulls = false
        }

    @SpoonacularRetrofit
    @Provides
    @Singleton
    fun provideSpoonacularRetrofit(
        @SpoonacularOkHttpClient okHttpClient: OkHttpClient,
        @SpoonacularJson json: Json,
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
    fun provideSpoonacularApiService(@SpoonacularRetrofit retrofit: Retrofit): SpoonacularApiService {
        return retrofit.create(SpoonacularApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeSearchRepository(
        @SpoonacularJson json: Json,
        service: SpoonacularApiService,
        cachedRecipeDao: CachedRecipeComplexDao
    ): RecipeSearchRepository {
        var apiKey: String?
        try {
             apiKey = System.getenv("SPOONACULAR_API_KEY")
            if (apiKey == null)
                apiKey = BuildConfig.SPOONACULAR_API_KEY
        } catch (_: Throwable) {
            apiKey = BuildConfig.SPOONACULAR_API_KEY
        }

        return RecipeSearchRepositoryImpl(
            apiKey = apiKey,
            service = service,
            cachedRecipeDao = cachedRecipeDao,
            json = json
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


@Module
@InstallIn(SingletonComponent::class)
object TranslateApiModule {

    @YandexTranslateRetrofit
    @Provides
    @Singleton
    fun provideTranslateApiRetrofit(
        @YandexTranslateJson json: Json,
        @YandexTranslateOkHttpClient client: OkHttpClient
    ) =
        Retrofit.Builder()
            .baseUrl("https://translate.api.cloud.yandex.net")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
    @Provides
    @Singleton
    fun provideTranslateApiService(@YandexTranslateRetrofit retrofit: Retrofit): TranslateApiService {
        return retrofit.create(TranslateApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTranslateApiRepository(service: TranslateApiService): TranslateApiRepository = TranslateApiRepositoryImpl(
        folderId = BuildConfig.FOLDER_ID,
        service = service
    )

    @YandexTranslateOkHttpClient
    @Provides
    @Singleton
    fun provideYandexTranslateOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val originalRequest = chain.request()

                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Api-Key ${BuildConfig.YANDEX_TRANSLATE_API_KEY}")
                    .header("Content-Type", "application/json")
                    .build()

                Log.d("TEST TAG", "URL: ${newRequest.url}")
                Log.d("TEST TAG", "Headers: ${newRequest.headers}")

                if (newRequest.body != null) {
                    val buffer = okio.Buffer()
                    newRequest.body!!.writeTo(buffer)
                    Log.d("TEST TAG", "Request Body: ${buffer.readUtf8()}")
                }

                val response = chain.proceed(newRequest)
                Log.d("TEST TAG", "Response code: ${response.code}")
                response
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @YandexTranslateJson
    @Provides
    @Singleton
    fun provideJson() =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            explicitNulls = false
        }
}