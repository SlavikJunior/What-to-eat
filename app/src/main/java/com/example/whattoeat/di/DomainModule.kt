package com.example.whattoeat.di

import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import com.example.whattoeat.domain.use_cases.AddFavoriteRecipeUseCase
import com.example.whattoeat.domain.use_cases.DeleteUsersRecipeUseCase
import com.example.whattoeat.domain.use_cases.GetFavoriteRecipesUseCase
import com.example.whattoeat.domain.use_cases.GetRecipesUseCase
import com.example.whattoeat.domain.use_cases.GetUsersRecipesUseCase
import com.example.whattoeat.domain.use_cases.RemoveFavoriteRecipeUseCase
import com.example.whattoeat.domain.use_cases.UploadUsersRecipeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideAddFavoriteRecipeUseCase(repository: FavoriteRecipeRepository) =
        AddFavoriteRecipeUseCase(
            repository = repository
        )

    @Provides
    fun provideRemoveFavoriteRecipeUseCase(repository: FavoriteRecipeRepository) =
        RemoveFavoriteRecipeUseCase(
            repository = repository
        )

    @Provides
    fun provideGetFavoriteRecipesUseCase(repository: FavoriteRecipeRepository) =
        GetFavoriteRecipesUseCase(
            repository = repository
        )

    @Provides
    fun provideGetRecipesUseCase(repository: RecipeSearchRepository) =
        GetRecipesUseCase(
            repository = repository
        )

    @Provides
    fun provideUploadUsersRecipeUseCase(repository: UsersRecipeRepository) =
        UploadUsersRecipeUseCase(
            repository = repository
        )

    @Provides
    fun provideDeleteUsersRecipeUseCase(repository: UsersRecipeRepository) =
        DeleteUsersRecipeUseCase(
            repository = repository
        )

    @Provides
    fun provideGetUsersRecipesUseCase(repository: UsersRecipeRepository) =
        GetUsersRecipesUseCase(
            repository = repository
        )
}