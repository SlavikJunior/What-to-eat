package com.example.whattoeat.presentation.ui.view_models

import androidx.lifecycle.ViewModel
import com.example.whattoeat.domain.use_cases.AddFavoriteRecipeUseCase
import com.example.whattoeat.domain.use_cases.RemoveFavoriteRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteRecipesViewModel @Inject constructor(
    private val addFavoriteRecipeUseCase: AddFavoriteRecipeUseCase,
    private val removeFavoriteRecipeUseCase: RemoveFavoriteRecipeUseCase
): ViewModel()