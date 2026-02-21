package com.example.whattoeat.presentation.ui.scaffoldElements

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import com.example.whattoeat.R
import com.example.whattoeat.presentation.ui.nav.Screen
import kotlinx.coroutines.delay

@SuppressLint("RestrictedApi")
@Composable
fun WhatToEatFloatingActionButton(
    backStack: NavBackStack<Screen>
) {
    val currentDestination = backStack.last()

    val isRecipeList = currentDestination is Screen.RecipeListDataObject

    if (isRecipeList) {
        FloatingActionButton(
            onClick = {
                backStack.add(Screen.UsersRecipesDataObject(openAddSheet = true))
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