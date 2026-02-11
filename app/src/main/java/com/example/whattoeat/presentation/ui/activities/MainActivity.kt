package com.example.whattoeat.presentation.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.whattoeat.presentation.ui.nav.WhatToEatNavHost
import com.example.whattoeat.presentation.ui.scaffoldElements.WhatToEatBottomAppBarNew
import com.example.whattoeat.presentation.ui.scaffoldElements.WhatToEatFloatingActionButton
import com.example.whattoeat.presentation.ui.scaffoldElements.WhatToEatTopAppBar
import com.example.whattoeat.presentation.ui.theme.WhatToEatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WhatToEatTheme {
                val navController = rememberNavController()

//                navController.addOnDestinationChangedListener { controller, _, _ ->
//                    val routes = controller
//                        .currentBackStack.value.joinToString(", ") { it.destination.route.toString() }
//
//                    Log.d("BackStackLog", "BackStack: $routes")
//                }

                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    floatingActionButton = { WhatToEatFloatingActionButton(navController = navController) },
                    contentWindowInsets = WindowInsets.Companion.systemBars,
                    topBar = { WhatToEatTopAppBar(navController = navController) },
                    bottomBar = {
                        WhatToEatBottomAppBarNew(navController = navController,)
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    WhatToEatNavHost(
                        navController = navController,
                        paddingValues = paddingValues,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}