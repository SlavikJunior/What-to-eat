package com.example.whattoeat.data.net.repository

import android.util.Log
import com.example.whattoeat.data.database.dao.CachedRecipeDao
import com.example.whattoeat.data.database.entity.CachedRecipe
import com.example.whattoeat.data.net.service.SpoonacularApiService
import com.example.whattoeat.domain.domain_entities.common.RecipeByIngredients
import com.example.whattoeat.domain.domain_entities.common.RecipeByIngredientsResult
import com.example.whattoeat.domain.domain_entities.common.RecipeComplexResult
import com.example.whattoeat.domain.domain_entities.common.RecipeFullInformation
import com.example.whattoeat.domain.domain_entities.common.RecipeFullInformationResult
import com.example.whattoeat.domain.domain_entities.common.RecipeResult
import com.example.whattoeat.domain.domain_entities.common.RecipeSimilar
import com.example.whattoeat.domain.domain_entities.common.RecipeSummary
import com.example.whattoeat.domain.domain_entities.common.Resource
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.domain.search.RecipeSearch
import com.example.whattoeat.domain.search.toQueryMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RecipeSearchRepositoryImpl @Inject constructor(
    val apiKey: String,
    val service: SpoonacularApiService,
    val cachedRecipeDao: CachedRecipeDao
) : RecipeSearchRepository {

    override suspend fun getRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch)=
        flow {
            emit(Resource.Loading())

            service.recipeComplexSearch(
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { response ->
                val resource = handleResponse(response)

                val recipeComplexResult = resource.data?.let { it as RecipeComplexResult }
                if (recipeComplexResult != null && recipeComplexResult.totalResults > 0) {
                    recipeComplexResult.recipeComplexList.forEach { recipeComplex ->
                        emit(Resource.Success(recipeComplex))
                    }
                } else
                    emit(Resource.Error("Not found :/"))
            }
        }

    override suspend fun getRecipeSimilar(recipeSearch: RecipeSearch.RecipeSimilarSearch): Flow<Resource<RecipeSimilar>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeSummary(recipeSearch: RecipeSearch.RecipeSummarySearch): Flow<Resource<RecipeSummary>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch) =
        flow<Resource<RecipeFullInformation>> {
            emit(Resource.Loading())

            service.recipeFullInformation(
                id = recipeSearch.id,
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { response ->
                val resource = handleResponse(response)

                val recipeFullInformationResult = resource.data?.let { it as RecipeFullInformationResult }
                if (recipeFullInformationResult != null)
                    emit(Resource.Success(recipeFullInformationResult.recipeFullInformationResult))
                else
                    emit(tryToGetFromCache(recipeSearch))
            }
        }

    override suspend fun getRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch) =
        flow<Resource<RecipeByIngredients>> {
            emit(Resource.Loading())

            service.recipeByIngredients(
                query = recipeSearch.toQueryMap(),
                apiKey = apiKey
            ).let { response ->
                val resource = handleResponse(response)

                val recipeByIngredientsResult = resource.data?.let { it as RecipeByIngredientsResult }
                if (recipeByIngredientsResult != null) {
                    recipeByIngredientsResult.recipeByIngredientsResult.forEach { recipeByIngredients ->

                        emit(Resource.Success(recipeByIngredients))
                    }
                } else emit(Resource.Error("Not found :/"))
            }
        }

    private fun handleResponse(response: Response<out RecipeResult>) =
        if (response.isSuccessful)
            Resource.Success(response.body())
        else {
            Log.e(TAG, "Response isn't successful, code: ${response.code()}")
            Resource.Error("Response isn't successful!")
        }

    @Suppress("UNCHECKED_CAST")
    private suspend fun tryToGetFromCache(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Resource<RecipeFullInformation> {
        var fromCachedRecipe: RecipeFullInformation? = null
        var cachedRecipe: CachedRecipe? = null
        withContext(Dispatchers.IO) {
           cachedRecipe  = async {
                cachedRecipeDao.selectById(recipeSearch.id)
            }.await()
        }

        if (cachedRecipe != null)
            fromCachedRecipe = cachedRecipe.toRecipeFullInformation()

        return if (fromCachedRecipe != null)
            Resource.Success(cachedRecipe) as Resource<RecipeFullInformation>
        else Resource.Error("Not found :/")
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}