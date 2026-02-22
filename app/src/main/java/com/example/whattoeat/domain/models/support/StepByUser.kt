package com.example.whattoeat.domain.models.support

import kotlinx.serialization.Serializable

@Serializable
data class StepByUser(
    val stepTitle: String,
    val image: String,
    val imageType: String,
    val number: Int,
    val step: String,
    val ingredients: List<Ingredient>
)