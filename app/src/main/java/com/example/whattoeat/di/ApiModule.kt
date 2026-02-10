package com.example.whattoeat.di

import android.util.Log
import com.example.whattoeat.BuildConfig
import com.example.whattoeat.data.net.adapter.ResultCallAdapterFactory
import com.example.whattoeat.data.yandex_translate.TranslateApiRepositoryImpl
import com.example.whattoeat.data.yandex_translate.TranslateApiService
import com.example.whattoeat.domain.repositories.TranslateApiRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

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
    fun provideTranslateApiRepository(service: TranslateApiService): TranslateApiRepository =
        TranslateApiRepositoryImpl(
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
