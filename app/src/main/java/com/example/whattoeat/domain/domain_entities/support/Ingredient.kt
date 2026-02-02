package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val id: Int,
    val name: String,
    val originalName: String,
    val amount: String,
    val unit: String
)
