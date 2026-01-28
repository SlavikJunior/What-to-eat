package com.example.whattoeat.presentation.ui.nav

import com.example.whattoeat.domain.domain_entities.common.Recipe
import kotlinx.serialization.Serializable

@Serializable
data object RecipeListDataObject

@Serializable
@JvmInline
value class RecipeDetailDataObject(
    private val recipe: Recipe
)

@Serializable
data object FavoriteRecipesDataObject