package com.example.whattoeat.presentation.ui.scaffoldElements

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.whattoeat.R
import com.example.whattoeat.presentation.ui.nav.BottomNavItem
import com.example.whattoeat.presentation.ui.nav.FavoriteRecipesDataObject
import com.example.whattoeat.presentation.ui.nav.RecipeListDataObject

@Composable
fun WhatToEatBottomAppBar(
    navController: NavHostController
) {
    BottomAppBar(
        modifier = Modifier
            .windowInsetsPadding(insets = WindowInsets.navigationBars)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = {
                    navController.navigate(RecipeListDataObject) {
                        launchSingleTop = true
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = stringResource(R.string.recipe_list_content_description)
                )
            }
            IconButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = { navController.navigate(FavoriteRecipesDataObject) }
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.favorite_recipes_content_description)
                )
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WhatToEatBottomAppBarPreview() =
    WhatToEatBottomAppBar(navController = rememberNavController())

@SuppressLint("RestrictedApi")
@Composable
fun WhatToEatBottomAppBarNew(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>
) {
    val items = bottomNavItems.sortedBy { it.index }

    // ←←← Главное изменение здесь
    val backStackEntries by navController.currentBackStack.collectAsState()

    val topBottomNavEntry = backStackEntries.lastOrNull { entry ->
        bottomNavItems.any { item ->
            entry.destination.route?.contains(item.getRouteString()) == true
        }
    }

    val selectedIndex = items.indexOfFirst { item ->
        topBottomNavEntry?.destination?.route?.contains(item.getRouteString()) == true
    }
    // ←←←

    NavigationBar {
        items.forEach { bottomNavItem ->
            NavigationBarItem(
                selected = selectedIndex == bottomNavItem.index,
                onClick = {
                    navController.navigate(bottomNavItem.route) {
                        // Лучше делать одинаково для всех табов
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
                        fontSize = 22.sp,
                    )
                }
            )
        }
    }
}