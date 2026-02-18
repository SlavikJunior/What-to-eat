package com.example.whattoeat.presentation.ui.nav

import kotlinx.serialization.Serializable

@Serializable
data object RecipeListDataObject

@Serializable
@JvmInline
value class RecipeDetailDataObject(val recipeId: Int)

@Serializable
data object FavoriteRecipesDataObject

@Serializable
@JvmInline
value class UsersRecipesDataObject(val openAddSheet: Boolean = false)

@Serializable
@JvmInline
value class UsersRecipeDetailDataObject(val recipeId: Int)