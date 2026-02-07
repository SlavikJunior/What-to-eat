package com.example.whattoeat.domain.use_cases

import com.example.whattoeat.data.mlkit.Languages
import com.example.whattoeat.domain.repositories.TranslateApiRepository
import javax.inject.Inject

class TranslateTextUseCase @Inject constructor(
    private val repository: TranslateApiRepository
) {

    suspend operator fun invoke(input: String, targetLang: Languages) =
        repository.translateText(input, targetLang)
}