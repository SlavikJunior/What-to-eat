package com.example.whattoeat.data.yandex_translate.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslateResponse(
    @SerialName("translations") val translations: List<Translation>
)

@Serializable
data class Translation(
    @SerialName("text") val text: String,
    @SerialName("detectedLanguageCode") val detectedLanguageCode: String? = null
)