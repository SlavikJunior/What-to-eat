package com.example.whattoeat.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.whattoeat.presentation.view_models.FavoriteRecipesViewModel

@Composable
fun FavoriteRecipes(
    navController: NavHostController,
    viewModel: FavoriteRecipesViewModel = viewModel(factory = FavoriteRecipesViewModel.Factory)
) {}