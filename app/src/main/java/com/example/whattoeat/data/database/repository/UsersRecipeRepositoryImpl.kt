package com.example.whattoeat.data.database.repository

import com.example.whattoeat.data.database.dao.UsersRecipeDao
import com.example.whattoeat.data.database.entity.UsersRecipe
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.Ingredient
import com.example.whattoeat.domain.repositories.UsersRecipeRepository
import com.example.whattoeat.domain.search.RecipeSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UsersRecipeRepositoryImpl(
    val usersRecipeDao: UsersRecipeDao
) : UsersRecipeRepository {
    override suspend fun uploadRecipe(recipe: Recipe.RecipeByUser): Long {
        val id: Long
        withContext(Dispatchers.IO) {
            id = async {
                usersRecipeDao.insert(UsersRecipe.fromRecipe(recipe))
            }.await()
        }
        return id
    }

    override suspend fun deleteRecipe(recipe: Recipe.RecipeByUser): Int {
        val cnt: Int
        withContext(Dispatchers.IO) {
            cnt = async {
                usersRecipeDao.delete(recipe.image)
            }.await()
        }
        return cnt
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun getRecipes(recipeSearch: RecipeSearch): Flow<Recipe.RecipeByUser> {
        val usersRecipes: Flow<UsersRecipe>
        withContext(Dispatchers.IO) {
            usersRecipes = when (recipeSearch) {
                is RecipeSearch.RecipeByIngredientsSearch -> {
                    val ingredients = recipeSearch.ingredients.split(",").map { it.trim() }.take(3)
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
        }
        return usersRecipes.map { recipe ->
            recipe.toRecipeByUser()
        }
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun getRecipesAsRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch): Flow<Recipe.RecipeComplex> {
        val usersRecipes = withContext(Dispatchers.IO) {
            val query = recipeSearch.query
            if (query != null) {
                usersRecipeDao.selectByTitle(query)
            } else
                throw IllegalArgumentException("Query title is null: $recipeSearch")
        }
        return usersRecipes.map { usersRecipe ->
            Recipe.RecipeComplex(
                id = usersRecipe.id,
                title = usersRecipe.title,
                image = usersRecipe.image,
                imageType = usersRecipe.imageType
            )
        }
    }

    override suspend fun getRecipesAsRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Flow<Recipe.RecipeFullInformation> {
        return withContext(Dispatchers.IO) {
            val recipe = usersRecipeDao.selectById(recipeSearch.id)
            if (recipe != null) {
                flowOf(recipe.toRecipeFullInformation())
            } else flow {}
        }
    }

    override suspend fun getRecipesAsRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch): Flow<Recipe.RecipeByIngredients> {
        val ingredients = recipeSearch.ingredients.split(",").map { it.trim() }.take(3)
        val usersRecipes: Flow<UsersRecipe>
        withContext(Dispatchers.IO) {
            usersRecipes = usersRecipeDao.selectByMultipleIngredients(
                ingredient1 = ingredients[0],
                ingredient2 = ingredients[1],
                ingredient3 = ingredients[2]
            )
        }


        return usersRecipes.map { usersRecipe ->
            usersRecipe.toRecipeByIngredients().copy(
                usedIngredients = usersRecipe.extendedIngredientsNames.filter { ingredient ->
                    ingredients.contains(ingredient)
                }.map {
                    Ingredient(
                        id = -1,
                        name = it,
                        originalName = null,
                        amount = null,
                        unit = null
                    )
                },
                missedIngredients = usersRecipe.extendedIngredientsNames.filter { ingredient ->
                    !ingredients.contains(ingredient)
                }.map {
                    Ingredient(
                        id = -1,
                        name = it,
                        originalName = null,
                        amount = null,
                        unit = null
                    )
                },
                unusedIngredients = ingredients.filter { ingredient ->
                    !usersRecipe.extendedIngredientsNames.contains(ingredient)
                }.map {
                    Ingredient(
                        id = -1,
                        name = it,
                        originalName = null,
                        amount = null,
                        unit = null
                    )
                }
            )
        }
    }
}