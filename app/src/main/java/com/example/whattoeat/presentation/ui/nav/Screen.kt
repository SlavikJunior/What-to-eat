package com.example.whattoeat.presentation.ui.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen: NavKey {

    @Serializable
    data object RecipeListScreen : Screen

    @Serializable
    @JvmInline
    value class RecipeDetailScreen(val recipeId: Int) : Screen

    @Serializable
    data object FavoriteRecipesScreen : Screen

    @Serializable
    @JvmInline
    value class UsersRecipesScreen(val openAddSheet: Boolean = false) : Screen

    @Serializable
    @JvmInline
    value class UsersRecipeDetailScreen(val recipeId: Int) : Screen
}