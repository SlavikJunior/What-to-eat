package com.example.whattoeat.data.mlkit

import com.example.whattoeat.domain.repositories.TranslateApiRepository
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import java.util.Locale

class TranslateApiRepositoryImpl() : TranslateApiRepository {

    private val languageIdentifier = LanguageIdentification.getClient()

    @Throws(Throwable::class)
    override suspend fun translateText(input: String, targetLanguage: Languages): String {
        var detectedLanguage: String = languageIdentifier.identifyLanguage(input).await()

        if (detectedLanguage == Languages.UNDEFINED.bcp47Code)
            detectedLanguage = Locale.getDefault().language

        val translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(detectedLanguage)
            .setTargetLanguage(targetLanguage.bcp47Code)
            .build()

        val translator = Translation.getClient(translatorOptions)

        return try {
            val conditions = DownloadConditions.Builder().requireWifi().build()
            translator.downloadModelIfNeeded(conditions).await()
            translator.translate(input).await()
        } catch (e: Throwable) {
            throw e
        } finally {
            translator.close()
        }
    }
}