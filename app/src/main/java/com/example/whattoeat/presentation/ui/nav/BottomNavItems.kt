package com.example.whattoeat.presentation.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface BottomNavItem {
    val title: String
    val imageVector: ImageVector
    val isStarted: Boolean
    val index: Int
    val route: Screen
}

data object RecipeListBottomNavItem : BottomNavItem {
    override val title = "Поиск"
    override val imageVector = Icons.AutoMirrored.Filled.List
    override val isStarted = true
    override val index = 1
    override val route = Screen.RecipeListScreen
}

data object FavoriteRecipesBottomNavItem : BottomNavItem {
    override val title = "Избранные"
    override val imageVector = Icons.Default.Favorite
    override val isStarted = false
    override val index = 2
    override val route = Screen.FavoriteRecipesScreen
}

data object UsersRecipesBottomNavItem : BottomNavItem {
    override val title = "Пользовательские"
    override val imageVector = Icons.Default.Edit
    override val isStarted = false
    override val index = 3
    override val route = Screen.UsersRecipesScreen()
}