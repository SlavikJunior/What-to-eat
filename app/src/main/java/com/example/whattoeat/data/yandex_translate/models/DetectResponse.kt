package com.example.whattoeat.data.yandex_translate.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetectResponse(
    @SerialName("languageCode") val languageCode: String
)