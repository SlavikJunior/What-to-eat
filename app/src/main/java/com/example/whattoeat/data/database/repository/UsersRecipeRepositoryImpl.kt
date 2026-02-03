package com.example.whattoeat.data.database.repository

import com.example.whattoeat.data.database.dao.UsersRecipeDao
import com.example.whattoeat.domain.domain_entities.common.RecipeByUser
import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UsersRecipeRepositoryImpl(
    val usersRecipeDao: UsersRecipeDao
) : UsersRecipeRepository {
    override suspend fun uploadRecipe(recipe: RecipeByUser): Long {
        val id: Long
        withContext(Dispatchers.IO) {
            id = async {
                usersRecipeDao.insert(recipe)
            }.await()
        }
        return id
    }

    override suspend fun deleteRecipe(recipe: RecipeByUser): Int {
        val cnt: Int
        withContext(Dispatchers.IO) {
            cnt = async {
                usersRecipeDao.delete(recipe.image)
            }.await()
        }
        return cnt
    }

    override suspend fun getRecipes(recipeSearch: RecipeSearch): Flow<RecipeByUser> {
        val usersRecipe = when (recipeSearch) {
            is RecipeSearch.RecipeByIngredientsSearch -> {
                val ingredients = recipeSearch.ingredients.split(",").take(3)
                usersRecipeDao.selectByMultipleIngredients(
                    ingredient1 = ingredients[0],
                    ingredient2 = ingredients[1],
                    ingredient3 = ingredients[2],
                )
            }

            is RecipeSearch.RecipeComplexSearch -> {
                val query = recipeSearch.query
                if (query != null) {
                    usersRecipeDao.selectByTitle(query)
                } else
                    throw IllegalArgumentException("Query title is null: $recipeSearch")
            }

            else -> throw IllegalArgumentException("Not supported search: $recipeSearch")
        }
        return usersRecipe.map { recipe ->
            recipe.toRecipeByUser()
        }
    }
}