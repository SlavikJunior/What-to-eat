package com.example.whattoeat.presentation.ui.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.whattoeat.presentation.ui.screens.FavoriteRecipes
import com.example.whattoeat.presentation.ui.screens.RecipeDetail
import com.example.whattoeat.presentation.ui.screens.RecipeList
import com.example.whattoeat.presentation.ui.screens.UsersRecipes

@Composable
fun WhatToEatNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues = PaddingValues(),
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = RecipeListDataObject
    ) {
        composable<RecipeListDataObject> { navBackStackEntry ->
            RecipeList(
                navController = navController,
                snackbarHostState = snackbarHostState,
                paddingValues = paddingValues
            )
        }
        composable<RecipeDetailDataObject> { navBackStackEntry ->
            val dataObject: RecipeDetailDataObject = navBackStackEntry.toRoute<RecipeDetailDataObject>()
            RecipeDetail(
                navController = navController,
                dataObject = dataObject,
                paddingValues = paddingValues
            )
        }
        composable<FavoriteRecipesDataObject> {
            FavoriteRecipes(
                navController = navController,
                paddingValues = paddingValues
            )
        }
        composable<UsersRecipesDataObject> {
            UsersRecipes(
                navController = navController,
                paddingValues = paddingValues
            )
        }
    }
}