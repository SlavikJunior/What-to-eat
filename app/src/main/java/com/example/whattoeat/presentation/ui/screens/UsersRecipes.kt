package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipesViewModel

@Composable
fun UsersRecipes(
    navController: NavHostController,
    viewModel: UsersRecipesViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues()
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(
                    red = 75f,
                    green = 65f,
                    blue = 65f,
                    alpha = 0.15f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(color = Color.White)
        ) {
            Text(
                text = "Not yet implemented :/",
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
fun UsersRecipesPreview() =
    UsersRecipes(navController = rememberNavController())