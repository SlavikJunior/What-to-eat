package com.example.whattoeat.presentation.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.navigation3.ui.NavDisplay
import com.example.whattoeat.presentation.ui.nav.Screen
import com.example.whattoeat.presentation.ui.scaffoldElements.WhatToEatBottomAppBar
import com.example.whattoeat.presentation.ui.scaffoldElements.WhatToEatFloatingActionButton
import com.example.whattoeat.presentation.ui.scaffoldElements.WhatToEatTopAppBar
import com.example.whattoeat.presentation.ui.screens.FavoriteRecipes
import com.example.whattoeat.presentation.ui.screens.RecipeDetail
import com.example.whattoeat.presentation.ui.screens.RecipeList
import com.example.whattoeat.presentation.ui.screens.UsersRecipeDetail
import com.example.whattoeat.presentation.ui.screens.UsersRecipes
import com.example.whattoeat.presentation.ui.theme.WhatToEatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhatToEatTheme {
                val backStack = rememberNavBackStack<Screen>(Screen.RecipeListScreen)

                Scaffold(
                    floatingActionButton = { WhatToEatFloatingActionButton(backStack = backStack) },
                    contentWindowInsets = WindowInsets.Companion.systemBars,
                    topBar = { WhatToEatTopAppBar(backStack = backStack) },
                    bottomBar = { WhatToEatBottomAppBar(backStack = backStack) },
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    App(backStack = backStack, paddingValues = paddingValues)
                }
            }
        }
    }
}

@Composable
fun App(backStack: NavBackStack<Screen>, paddingValues: PaddingValues) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Screen.RecipeListScreen> {
                RecipeList(backStack = backStack, paddingValues = paddingValues)
            }
            entry<Screen.RecipeDetailScreen> {
                RecipeDetail(backStack = backStack, dataObject = it, paddingValues = paddingValues)
            }
            entry<Screen.FavoriteRecipesScreen> {
                FavoriteRecipes(backStack = backStack, paddingValues = paddingValues)
            }
            entry<Screen.UsersRecipesScreen> { dataObject ->
                UsersRecipes(backStack = backStack, openAddSheet = dataObject.openAddSheet, paddingValues = paddingValues)
            }
            entry<Screen.UsersRecipeDetailScreen> { dataObject ->
                UsersRecipeDetail(dataObject = dataObject, paddingValues = paddingValues)
            }
        }
    )
}

@Composable
private fun <T : NavKey> rememberNavBackStack(vararg elements: T): NavBackStack<T> {
    return rememberSerializable(
        serializer = NavBackStackSerializer(elementSerializer = NavKeySerializer())
    ) {
        NavBackStack(*elements)
    }
}