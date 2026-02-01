package com.example.whattoeat.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.whattoeat.presentation.ui.view_models.FavoriteRecipesViewModel

@Composable
fun FavoriteRecipes(
    navController: NavHostController,
    viewModel: FavoriteRecipesViewModel = hiltViewModel()
) {}