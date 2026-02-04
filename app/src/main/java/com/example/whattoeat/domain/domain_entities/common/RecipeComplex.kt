package com.example.whattoeat.domain.domain_entities.common

import kotlinx.serialization.Serializable

@Serializable
data class RecipeComplex(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String
): Recipe

@Serializable
data class RecipeComplexResult(
    val recipeComplexList: List<RecipeComplex>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
): RecipeResult