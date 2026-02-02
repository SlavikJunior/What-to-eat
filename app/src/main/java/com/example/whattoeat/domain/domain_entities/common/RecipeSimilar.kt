package com.example.whattoeat.domain.domain_entities.common

import kotlinx.serialization.Serializable

@Serializable
data class RecipeSimilar(
    val id: Int,
    val image: String,
    val imageType: String,
    val title: String,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceUrl: String
)