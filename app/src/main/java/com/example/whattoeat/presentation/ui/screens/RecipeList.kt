package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.whattoeat.presentation.view_models.RecipeListViewModel

@Composable
fun RecipeList(
    navController: NavHostController,
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: RecipeListViewModel = viewModel(factory = RecipeListViewModel.Factory)
) {

}