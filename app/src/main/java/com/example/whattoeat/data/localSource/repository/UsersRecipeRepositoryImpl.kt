package com.example.whattoeat.data.localSource.repository

import com.example.whattoeat.data.localSource.dao.UsersRecipeDao
import com.example.whattoeat.data.localSource.entity.UsersRecipe
import com.example.whattoeat.di.IoDispatcher
import com.example.whattoeat.domain.models.common.Recipe
import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UsersRecipeRepositoryImpl(
    @IoDispatcher
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    val usersRecipeDao: UsersRecipeDao
) : UsersRecipeRepository {
    override suspend fun updateRecipe(recipe: Recipe.RecipeByUser) =
        withContext(ioDispatcher) {
            usersRecipeDao.update(
                id = recipe.id,
                title = recipe.title,
                readyInMinutes = recipe.readyInMinutes,
                servings = recipe.servings,
                ingredients = recipe.ingredients,
                notes = recipe.notes
            )
        }

    override suspend fun uploadRecipe(recipe: Recipe.RecipeByUser) =
        withContext(ioDispatcher) {
            usersRecipeDao.insert(UsersRecipe.fromRecipe(recipe))
        }

    override suspend fun deleteRecipe(recipe: Recipe.RecipeByUser) =
        withContext(ioDispatcher) {
                usersRecipeDao.delete(recipe.title)
        }

    override suspend fun getAllRecipes(): Flow<List<Recipe.RecipeByUser>> {
        return usersRecipeDao.selectAll()?.map { userRecipesList -> userRecipesList.map { it.toRecipeByUser() } }
            ?.flowOn(ioDispatcher) ?: flowOf(emptyList())
    }

    override suspend fun getRecipeById(id: Int): Recipe.RecipeByUser? {
        return withContext(ioDispatcher){
            usersRecipeDao.selectById(id)?.toRecipeByUser()
        }
    }
}