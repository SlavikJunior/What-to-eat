package com.example.whattoeat.presentation.ui.bars

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.whattoeat.R
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
                modifier = Modifier.fillMaxWidth().weight(1f),
                onClick = {
                    navController.navigate(RecipeListDataObject) {
                        launchSingleTop = true
                    }
                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = stringResource(R.string.recipe_list_content_description))
            }
            IconButton(
                modifier = Modifier.fillMaxWidth().weight(1f),
                onClick = { navController.navigate(FavoriteRecipesDataObject) }
            ) {
                Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = stringResource(R.string.favorite_recipes_content_description))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WhatToEatBottomAppBarPreview() =
    WhatToEatBottomAppBar(navController = rememberNavController())