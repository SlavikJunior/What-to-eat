package com.example.whattoeat.data.translateApi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetectRequest(
    @SerialName("text") val text: String,
    @SerialName("folderId") val folderId: String
)