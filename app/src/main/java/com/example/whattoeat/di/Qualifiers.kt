package com.example.whattoeat.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SpoonacularRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YandexTranslateRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SpoonacularOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YandexTranslateOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SpoonacularJson

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YandexTranslateJson