package com.example.whattoeat.domain.domainEntities.support

import kotlinx.serialization.Serializable

@Serializable
data class Step(
    val number: Int,
    val step: String,
    val ingredients: List<Ingredient>
)