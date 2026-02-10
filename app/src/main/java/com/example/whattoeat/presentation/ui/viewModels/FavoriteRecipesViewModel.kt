package com.example.whattoeat.presentation.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.whattoeat.di.IoDispatcher
import com.example.whattoeat.domain.use_cases.AddFavoriteRecipeUseCase
import com.example.whattoeat.domain.use_cases.RemoveFavoriteRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class FavoriteRecipesViewModel @Inject constructor(
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val addFavoriteRecipe: AddFavoriteRecipeUseCase,
    private val removeFavoriteRecipe: RemoveFavoriteRecipeUseCase
): ViewModel()