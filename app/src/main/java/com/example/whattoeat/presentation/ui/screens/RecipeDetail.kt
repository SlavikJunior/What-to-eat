package com.example.whattoeat.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.whattoeat.presentation.ui.nav.RecipeDetailDataObject
import com.example.whattoeat.presentation.view_models.RecipeDetailViewModel

@Composable
fun RecipeDetail(
    dataObject: RecipeDetailDataObject,
    navController: NavHostController,
    viewModel: RecipeDetailViewModel = viewModel(factory = RecipeDetailViewModel.Factory)
) {}