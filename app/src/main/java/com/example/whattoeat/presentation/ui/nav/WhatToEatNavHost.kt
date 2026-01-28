package com.example.whattoeat.presentation.ui.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.whattoeat.presentation.ui.screens.FavoriteRecipes
import com.example.whattoeat.presentation.ui.screens.RecipeDetail
import com.example.whattoeat.presentation.ui.screens.RecipeList

@Composable
fun WhatToEatNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues = PaddingValues()
) {
    NavHost(
        navController = navController,
        startDestination = RecipeListDataObject
    ) {
        composable<RecipeListDataObject> {
            RecipeList(navController = navController, paddingValues = paddingValues)
        }
        composable<RecipeDetailDataObject> { navBackStackEntry ->
            val arg: RecipeDetailDataObject = navBackStackEntry.toRoute<RecipeDetailDataObject>()
            RecipeDetail(dataObject = arg, navController = navController)
        }
        composable<FavoriteRecipesDataObject> {
            FavoriteRecipes(navController = navController)
        }
    }
}