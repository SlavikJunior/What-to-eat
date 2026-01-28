package com.example.whattoeat.presentation.ui.bars

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.whattoeat.R
import com.example.whattoeat.presentation.ui.nav.FavoriteRecipesDataObject
import com.example.whattoeat.presentation.ui.nav.RecipeListDataObject

@Composable
fun WhatToEatBottomAppBar(
    navController: NavHostController
) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigate(RecipeListDataObject) }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = stringResource(R.string.recipe_list_content_description))
            }
            IconButton(
                onClick = { navController.navigate(FavoriteRecipesDataObject) }
            ) {
                Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = stringResource(R.string.favorite_recipes_content_description))
            }
        }
    }
}