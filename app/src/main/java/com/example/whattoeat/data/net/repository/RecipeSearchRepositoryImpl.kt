package com.example.whattoeat.data.net.repository

import android.util.Log
import com.example.whattoeat.data.database.dao.CachedRecipeDao
import com.example.whattoeat.data.database.dao.UsersRecipeDao
import com.example.whattoeat.data.net.service.SpoonacularApiService
import com.example.whattoeat.data.net.utill.NotSuccessfulSearch
import com.example.whattoeat.domain.domain_entities.common.*
import com.example.whattoeat.domain.repositories.RecipeSearchRepository
import com.example.whattoeat.domain.search.RecipeSearch
import com.example.whattoeat.domain.search.toQueryMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class RecipeSearchRepositoryImpl @Inject constructor(
    val apiKey: String,
    val service: SpoonacularApiService,
    val usersRecipeDao: UsersRecipeDao,
    val cachedRecipeDao: CachedRecipeDao
) : RecipeSearchRepository {

    @Throws(NotSuccessfulSearch::class)
    override suspend fun getRecipeComplex(recipeSearch: RecipeSearch.RecipeComplexSearch) =
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
                } else throw NotSuccessfulSearch(recipeSearch.toString())
            }
        }

    override suspend fun getRecipeSimilar(recipeSearch: RecipeSearch.RecipeSimilarSearch): Flow<Resource<RecipeSimilar>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeSummary(recipeSearch: RecipeSearch.RecipeSummarySearch): Flow<Resource<RecipeSummary>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeFullInformation(recipeSearch: RecipeSearch.RecipeFullInformationSearch): Flow<Resource<RecipeFullInformation>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeByIngredients(recipeSearch: RecipeSearch.RecipeByIngredientsSearch): Flow<Resource<RecipeByIngredients>> {
        TODO("Not yet implemented")
    }

    private fun handleResponse(response: Response<out RecipeResult>) =
        if (response.isSuccessful)
            Resource.Success(response.body())
        else {
            Log.e(TAG, "Response isn't successful, code: ${response.code()}")
            Resource.Error("Response isn't successful!")
        }

    companion object {
        private const val TAG = "TEST TAG"
    }
}