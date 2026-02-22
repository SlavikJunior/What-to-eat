package com.example.whattoeat.domain.models.support

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val id: Int,
    val name: String,
    @Transient val originalName: String? = null,
    @Transient val amount: Double? = null,
    @Transient val unit: String? = null
)
