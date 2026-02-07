package com.example.whattoeat.data.yandex_translate.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslateRequest(
    @SerialName("folderId")
    val folderId: String,
    @SerialName("texts")
    val texts: List<String>,
    @SerialName("targetLanguageCode")
    val targetLanguageCode: String = "en",
    @SerialName("sourceLanguageCode")
    val sourceLanguageCode: String? = null,
    @SerialName("format")
    val format: String = "PLAIN_TEXT"
)