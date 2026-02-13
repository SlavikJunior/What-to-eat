package com.example.whattoeat.presentation.ui.scaffoldElements

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.whattoeat.R
import com.example.whattoeat.presentation.ui.nav.RecipeListDataObject
import com.example.whattoeat.presentation.ui.nav.UsersRecipesDataObject

@SuppressLint("RestrictedApi")
@Composable
fun WhatToEatFloatingActionButton(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isRecipeList = currentDestination?.hasRoute<RecipeListDataObject>() == true

    if (isRecipeList) {
        FloatingActionButton(
            onClick = {
                navController.navigate(route = UsersRecipesDataObject) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = "Upload recipe",
                    modifier = Modifier.padding(16.dp)
                )
            },
            modifier = Modifier
                .padding(all = 8.dp)
                .size(64.dp)
        )
    }
}