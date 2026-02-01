package com.example.whattoeat.presentation.ui.screens

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.whattoeat.presentation.ui.nav.RecipeDetailDataObject
import com.example.whattoeat.presentation.ui.view_models.RecipeDetailViewModel

@Composable
fun RecipeDetail(
    dataObject: RecipeDetailDataObject,
    navController: NavHostController,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {}