package com.example.whattoeat.domain.repositories

import com.example.whattoeat.data.mlkit.Languages

interface TranslateApiRepository {

    @Throws(Throwable::class)
    suspend fun translateText(input: String , targetLanguage: Languages = Languages.ENGLISH): String
}