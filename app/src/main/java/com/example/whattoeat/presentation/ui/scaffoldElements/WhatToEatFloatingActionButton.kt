package com.example.whattoeat.presentation.ui.scaffoldElements

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import com.example.whattoeat.R
import com.example.whattoeat.presentation.ui.nav.Screen

@Composable
fun WhatToEatFloatingActionButton(
    backStack: NavBackStack<Screen>
) {
    val currentDestination = backStack.last()

    val isRecipeList = currentDestination is Screen.RecipeListScreen

    if (isRecipeList) {
        FloatingActionButton(
            onClick = {
                backStack.add(Screen.UsersRecipesScreen(openAddSheet = true))
            },
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = stringResource(R.string.upload_users_recipe_content_description),
                    modifier = Modifier.padding(16.dp)
                )
            },
            modifier = Modifier
                .padding(all = 8.dp)
                .size(64.dp)
        )
    }
}