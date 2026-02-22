package com.example.whattoeat.domain.repositories

import com.example.whattoeat.data.translateApi.models.Languages

interface TranslateApiRepository {

    @Throws(Throwable::class)
    suspend fun translateText(input: List<String> , targetLanguage: Languages = Languages.ENGLISH): List<String>
}