package com.example.whattoeat.presentation.ui.nav

import androidx.compose.foundation.layout.PaddingValues
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
) {
    NavHost(
        navController = navController,
        startDestination = RecipeListDataObject
    ) {
        composable<RecipeListDataObject> { navBackStackEntry ->
            RecipeList(
                navController = navController,
                paddingValues = paddingValues
            )
        }
        composable<RecipeDetailDataObject> { navBackStackEntry ->
            val dataObject: RecipeDetailDataObject = navBackStackEntry.toRoute<RecipeDetailDataObject>()
            RecipeDetail(
                navController = navController,
                paddingValues = paddingValues,
                dataObject = dataObject
            )
        }
        composable<FavoriteRecipesDataObject> {
            FavoriteRecipes(
                navController = navController,
                paddingValues = paddingValues
            )
        }
        composable<UsersRecipesDataObject> { navBackStackEntry ->
            val data: UsersRecipesDataObject = navBackStackEntry.toRoute()

            UsersRecipes(
                navController = navController,
                paddingValues = paddingValues,
                openAddSheet = data.openAddSheet
            )
        }
    }
}