package com.example.whattoeat.presentation.view_models.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whattoeat.data.net.client.RussianFoodComClient
import com.example.whattoeat.data.net.repository.RecipeSearchRepositoryImpl
import com.example.whattoeat.domain.use_cases.GetRecipesUC
import com.example.whattoeat.presentation.view_models.RecipeListViewModel

abstract class RecipeListViewModelDefaultFactory: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        RecipeListViewModel(
            getRecipesUseCase = GetRecipesUC(
                repository = RecipeSearchRepositoryImpl(
                    client = RussianFoodComClient()
                )
            )
        ) as T
}