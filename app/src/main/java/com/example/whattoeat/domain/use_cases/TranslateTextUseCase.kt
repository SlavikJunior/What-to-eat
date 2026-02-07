package com.example.whattoeat.domain.use_cases

import com.example.whattoeat.data.yandex_translate.models.Languages
import com.example.whattoeat.domain.repositories.TranslateApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TranslateTextUseCase @Inject constructor(
    private val repository: TranslateApiRepository
) {

    suspend operator fun invoke(input: List<String>, targetLang: Languages = Languages.ENGLISH) =
        withContext(Dispatchers.IO) {
            repository.translateText(input, targetLang)
        }
}