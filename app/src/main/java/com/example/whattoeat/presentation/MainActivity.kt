package com.example.whattoeat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
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
                    contentWindowInsets = WindowInsets.systemBars,
                    topBar = { WhatToEatTopAppBar(navController = navController) },
                    bottomBar = { WhatToEatBottomAppBar(navController = navController) },
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    WhatToEatNavHost(navController = navController, paddingValues = paddingValues)
                }
            }
        }
    }
}