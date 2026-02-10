package com.example.whattoeat.di

import android.content.Context
import com.example.whattoeat.BuildConfig
import com.example.whattoeat.data.database.dao.CachedRecipeComplexDao
import com.example.whattoeat.data.net.adapter.ResultCallAdapterFactory
import com.example.whattoeat.data.net.repository.RecipeSearchRepositoryImpl
import com.example.whattoeat.data.net.service.SpoonacularApiService
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
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
object RemoteSourceModule {

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

}