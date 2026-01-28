package com.example.whattoeat.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.whattoeat.data.database.repository.RecipeRepository
import com.example.whattoeat.domain.use_cases.AddFavoriteRecipeUC
import com.example.whattoeat.domain.use_cases.RemoveFavoriteRecipeUC

class RecipeDetailViewModel(
    private val addFavoriteRecipeUseCase: AddFavoriteRecipeUC,
    private val removeFavoriteRecipeUseCase: RemoveFavoriteRecipeUC
): ViewModel() {

    companion object Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            RecipeDetailViewModel(
                addFavoriteRecipeUseCase = AddFavoriteRecipeUC(
                    repository = RecipeRepository(
                        recipeDAO = TODO() // todo: продумать, как я могу поставить сюда DAO, mb через контейнер, но как его сюда прокинуть??
                    )
                ),
                removeFavoriteRecipeUseCase = RemoveFavoriteRecipeUC(
                    repository = RecipeRepository(
                        recipeDAO = TODO() // todo: продумать, как я могу поставить сюда DAO, mb через контейнер, но как его сюда прокинуть??
                    )
                )
            ) as T
    }
}