package com.example.whattoeat.presentation.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.whattoeat.presentation.ui.nav.FavoriteRecipesBottomNavItem
import com.example.whattoeat.presentation.ui.nav.RecipeListBottomNavItem
import com.example.whattoeat.presentation.ui.nav.UsersRecipesBottomNavItem
import com.example.whattoeat.presentation.ui.nav.WhatToEatNavHost
import com.example.whattoeat.presentation.ui.scaffoldElements.WhatToEatBottomAppBar
import com.example.whattoeat.presentation.ui.scaffoldElements.WhatToEatBottomAppBarNew
import com.example.whattoeat.presentation.ui.scaffoldElements.WhatToEatFloatingActionButton
import com.example.whattoeat.presentation.ui.scaffoldElements.WhatToEatTopAppBar
import com.example.whattoeat.presentation.ui.theme.WhatToEatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhatToEatTheme {
                val navController = rememberNavController()
                Scaffold(
                    floatingActionButton = { WhatToEatFloatingActionButton(navController = navController) },
                    contentWindowInsets = WindowInsets.Companion.systemBars,
                    topBar = { WhatToEatTopAppBar(navController = navController) },
                    bottomBar = { WhatToEatBottomAppBarNew(
                        navController = navController,
                        bottomNavItems = listOf(
                            RecipeListBottomNavItem,
                            FavoriteRecipesBottomNavItem,
                            UsersRecipesBottomNavItem
                        )
                    ) },
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    WhatToEatNavHost(navController = navController, paddingValues = paddingValues)
                }
            }
        }
    }
}