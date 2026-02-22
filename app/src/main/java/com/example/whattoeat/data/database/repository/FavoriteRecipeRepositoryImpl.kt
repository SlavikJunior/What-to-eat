package com.example.whattoeat.data.database.repository

import com.example.whattoeat.data.database.dao.FavoriteRecipeDao
import com.example.whattoeat.data.database.entity.FavoriteRecipe
import com.example.whattoeat.di.IoDispatcher
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRecipeRepositoryImpl @Inject constructor(
    @IoDispatcher
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    val favoriteRecipeDao: FavoriteRecipeDao
) : FavoriteRecipeRepository {
    override suspend fun addRecipe(recipe: Recipe): Long {
        val id: Long

        when (recipe) {
            is Recipe.RecipeComplex -> {
                withContext(ioDispatcher) {
                    id = favoriteRecipeDao.insert(FavoriteRecipe.fromRecipeComplex(recipe))
                    return@withContext id
                }
            }

            is Recipe.RecipeByIngredients -> {
                withContext(ioDispatcher) {
                    id = favoriteRecipeDao.insert(FavoriteRecipe.fromRecipeByIngredients(recipe))
                    return@withContext id
                }
            }

            is Recipe.RecipeSimilar -> {
                withContext(ioDispatcher) {
                    id = favoriteRecipeDao.insert(FavoriteRecipe.fromRecipeSimilar(recipe))
                    return@withContext id
                }
            }

            is Recipe.RecipeFullInformation -> {
                withContext(ioDispatcher) {
                    id = favoriteRecipeDao.insert(FavoriteRecipe.fromRecipeFullInformation(recipe))
                    return@withContext id
                }
            }

            else -> return -1
        }

        return -1
    }

    override suspend fun removeRecipe(recipe: Recipe): Int {
        val cnt: Int

        when (recipe) {
            is Recipe.RecipeComplex -> {
                withContext(ioDispatcher) {
                    cnt = favoriteRecipeDao.delete(FavoriteRecipe.fromRecipeComplex(recipe))
                    return@withContext cnt
                }
            }

            is Recipe.RecipeByIngredients -> {
                withContext(ioDispatcher) {
                    cnt = favoriteRecipeDao.delete(FavoriteRecipe.fromRecipeByIngredients(recipe))
                    return@withContext cnt
                }
            }

            is Recipe.RecipeSimilar -> {
                withContext(ioDispatcher) {
                    cnt = favoriteRecipeDao.delete(FavoriteRecipe.fromRecipeSimilar(recipe))
                    return@withContext cnt
                }
            }

            is Recipe.RecipeFullInformation -> {
                withContext(ioDispatcher) {
                    cnt = favoriteRecipeDao.delete(FavoriteRecipe.fromRecipeFullInformation(recipe))
                    return@withContext cnt
                }
            }

            else -> return -1
        }

        return -1
    }

    override suspend fun getRecipes(): Flow<List<FavoriteRecipe>> = favoriteRecipeDao.selectAll()

    override suspend fun isFavorite(id: Int): Boolean {
        var selected: FavoriteRecipe? = null
        withContext(Dispatchers.IO) {
            selected = favoriteRecipeDao.selectById(id)
        }
        return selected != null
    }
}