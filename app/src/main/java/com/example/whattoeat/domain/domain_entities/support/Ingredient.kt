package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val id: Int,
    val name: String,
    @Transient val originalName: String = "",
    @Transient val amount: Double = 0.0,
    @Transient val unit: String = ""
)
