package com.example.whattoeat.domain.domain_entities.common

import kotlinx.serialization.Serializable

@Serializable
data class RecipeSimple(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String
)