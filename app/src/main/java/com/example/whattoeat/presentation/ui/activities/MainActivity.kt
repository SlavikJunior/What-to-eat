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
import com.example.whattoeat.presentation.ui.bars.WhatToEatBottomAppBar
import com.example.whattoeat.presentation.ui.bars.WhatToEatTopAppBar
import com.example.whattoeat.presentation.ui.nav.WhatToEatNavHost
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
                    contentWindowInsets = WindowInsets.Companion.systemBars,
                    topBar = { WhatToEatTopAppBar(navController = navController) },
                    bottomBar = { WhatToEatBottomAppBar(navController = navController) },
                    modifier = Modifier.Companion.fillMaxSize()
                ) { paddingValues ->
                    WhatToEatNavHost(navController = navController, paddingValues = paddingValues)
                }
            }
        }
    }
}