package com.example.whattoeat.data.mlkit

import android.util.Log
import com.example.whattoeat.domain.repositories.TranslateApiRepository
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import java.util.Locale

class TranslateApiRepositoryImpl() : TranslateApiRepository {

    private val languageIdentifier = LanguageIdentification.getClient()

    @Throws(Throwable::class)
    override suspend fun translateText(input: String, targetLanguage: Languages): String {
        Log.d(TAG, "Input text: $input")

        var detectedLanguage: String = languageIdentifier.identifyLanguage(input).await()

        Log.d(TAG, "Detected language by mlkit: $detectedLanguage")

        if (detectedLanguage == Languages.ENGLISH.bcp47Code) return input

        if (detectedLanguage == Languages.UNDEFINED.bcp47Code) {
            detectedLanguage = Locale.getDefault().language

            Log.d(TAG, "Detected language from locale: $detectedLanguage")
        }

        val translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.RUSSIAN)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()

        val translator = Translation.getClient(translatorOptions)

        return try {
            val conditions = DownloadConditions.Builder().requireWifi().build()
            translator.downloadModelIfNeeded(conditions).await()
            val result = translator.translate(input).await()

            Log.d(TAG, "From $input on $detectedLanguage language translated to $result on $targetLanguage language")

            result
        } catch (e: Throwable) {
            throw e
        } finally {
            translator.close()
        }
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}