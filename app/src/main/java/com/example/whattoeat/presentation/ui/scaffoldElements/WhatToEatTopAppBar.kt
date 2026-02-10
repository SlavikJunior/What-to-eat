package com.example.whattoeat.presentation.ui.scaffoldElements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.whattoeat.R
import com.example.whattoeat.presentation.ui.nav.FavoriteRecipesDataObject
import com.example.whattoeat.presentation.ui.nav.RecipeListDataObject
import com.example.whattoeat.presentation.ui.nav.UsersRecipesDataObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatToEatTopAppBar(
    navController: NavHostController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = backStackEntry?.destination?.route.orEmpty()

    val isBottomNavRoot = listOf(
        RecipeListDataObject::class.java.simpleName,
        FavoriteRecipesDataObject::class.java.simpleName,
        UsersRecipesDataObject::class.java.simpleName
    ).any { currentRoute.contains(it, ignoreCase = true) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (!isBottomNavRoot) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_content_description)
                    )
                }
            }
        }
    )
}