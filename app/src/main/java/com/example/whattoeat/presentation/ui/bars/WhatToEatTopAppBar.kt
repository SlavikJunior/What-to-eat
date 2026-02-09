package com.example.whattoeat.presentation.ui.bars

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
import com.example.whattoeat.presentation.ui.nav.RecipeListDataObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatToEatTopAppBar(
    navController: NavHostController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = backStackEntry?.destination?.route.orEmpty()

    val isRecipeList = currentRoute.contains(
        other = RecipeListDataObject::class.java.simpleName,
        ignoreCase = true
    )

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (!isRecipeList) {
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