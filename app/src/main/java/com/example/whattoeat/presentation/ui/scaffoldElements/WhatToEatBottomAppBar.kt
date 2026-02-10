package com.example.whattoeat.presentation.ui.scaffoldElements

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.whattoeat.presentation.ui.nav.BottomNavItem
import com.example.whattoeat.presentation.ui.nav.FavoriteRecipesBottomNavItem
import com.example.whattoeat.presentation.ui.nav.RecipeListBottomNavItem
import com.example.whattoeat.presentation.ui.nav.UsersRecipesBottomNavItem

@SuppressLint("RestrictedApi")
@Composable
fun WhatToEatBottomAppBarNew(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem> = listOf(
        RecipeListBottomNavItem,
        FavoriteRecipesBottomNavItem,
        UsersRecipesBottomNavItem
    )
) {
    val items = bottomNavItems.sortedBy { it.index }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier
            .windowInsetsPadding(insets = WindowInsets.navigationBars)
            .fillMaxWidth()
    ) {
        items.forEach { bottomNavItem ->
            val isSelected = currentDestination?.hierarchy?.any { destination ->
                destination.hasRoute(bottomNavItem.route::class)
            } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(bottomNavItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = bottomNavItem.imageVector,
                        contentDescription = bottomNavItem.title
                    )
                },
                label = {
                    Text(
                        text = bottomNavItem.title.lowercase(),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            )
        }
    }
}