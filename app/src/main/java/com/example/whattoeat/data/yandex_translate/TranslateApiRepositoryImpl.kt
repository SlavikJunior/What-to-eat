package com.example.whattoeat.data.yandex_translate

import android.util.Log
import com.example.whattoeat.data.yandex_translate.models.DetectRequest
import com.example.whattoeat.data.yandex_translate.models.Languages
import com.example.whattoeat.data.yandex_translate.models.TranslateRequest
import com.example.whattoeat.domain.repositories.TranslateApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TranslateApiRepositoryImpl @Inject constructor(
    private val folderId: String,
    private val service: TranslateApiService
) : TranslateApiRepository {

    override suspend fun translateText(
        input: List<String>,
        targetLanguage: Languages
    ): List<String> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Input text: $input")

        val nonEmptyTexts = input.filter { it.isNotBlank() }
        if (nonEmptyTexts.isEmpty()) {
            Log.d(TAG, "No text to translate, returning original")
            return@withContext input
        }

        try {
            val detectRequest = DetectRequest(
                text = nonEmptyTexts.first(),
                folderId = folderId
            )

            Log.d(TAG, "Sending detect request: $detectRequest")
            val detectResponse = service.detect(detectRequest)
            Log.d(TAG, "Detected language: ${detectResponse.languageCode}")

            if (detectResponse.languageCode.equals(targetLanguage.bcp47Code, ignoreCase = true)) {
                Log.d(TAG, "Text is already English, skipping translation")
                return@withContext input
            }

            val translateRequest = TranslateRequest(
                folderId = folderId,
                texts = nonEmptyTexts,
                targetLanguageCode = targetLanguage.bcp47Code
            )

            Log.d(TAG, "Sending translate request for texts: $nonEmptyTexts")
            val translateResponse = service.translate(translateRequest)

            val translatedTexts = translateResponse.translations.map { it.text }
            Log.d(TAG, "Translated texts: $translatedTexts")

            val result = mutableListOf<String>()
            var translatedIndex = 0

            for (text in input) {
                if (text.isBlank()) {
                    result.add("")
                } else {
                    result.add(translatedTexts.getOrElse(translatedIndex) { text })
                    translatedIndex++
                }
            }

            Log.d(TAG, "Final result: $result")
            return@withContext result

        } catch (e: Exception) {
            Log.e(TAG, "Translation error: ${e.message}", e)
            return@withContext input
        }
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}