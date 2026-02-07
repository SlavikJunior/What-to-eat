package com.example.whattoeat.domain.repositories

interface TranslateApiRepository {

    suspend fun translateText(input: String , targetLang: String): String
}