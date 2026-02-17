package com.example.whattoeat.data.database.repository

import com.example.whattoeat.data.database.dao.FavoriteRecipeDao
import com.example.whattoeat.data.database.entity.FavoriteRecipe
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.domain.repositories.FavoriteRecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRecipeRepositoryImpl @Inject constructor(
    val favoriteRecipeDao: FavoriteRecipeDao
) : FavoriteRecipeRepository {
    override suspend fun addRecipe(recipe: Recipe): Long {
        val id: Long

        if (recipe is Recipe.RecipeComplex) {
            withContext(Dispatchers.IO) {
                id = async {
                    favoriteRecipeDao.insert(FavoriteRecipe.fromRecipeComplex(recipe))
                }.await()
            }
            return id
        }

        return -1
    }

    override suspend fun removeRecipe(recipe: Recipe): Int {
        val cnt: Int

        if (recipe is Recipe.RecipeComplex) {
            withContext(Dispatchers.IO) {
                cnt = async {
                    favoriteRecipeDao.selectById(recipe.id)?.let {
                        favoriteRecipeDao.delete(it)
                    } ?: -1
                }.await()
            }
            return cnt
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