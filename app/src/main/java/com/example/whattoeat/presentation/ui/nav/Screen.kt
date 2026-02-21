package com.example.whattoeat.presentation.ui.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen: NavKey {

    @Serializable
    data object RecipeListDataObject : Screen

    @Serializable
    @JvmInline
    value class RecipeDetailDataObject(val recipeId: Int) : Screen

    @Serializable
    data object FavoriteRecipesDataObject : Screen

    @Serializable
    @JvmInline
    value class UsersRecipesDataObject(val openAddSheet: Boolean = false) : Screen

    @Serializable
    @JvmInline
    value class UsersRecipeDetailDataObject(val recipeId: Int) : Screen
}