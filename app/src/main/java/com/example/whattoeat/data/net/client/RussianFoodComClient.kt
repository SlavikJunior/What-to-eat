package com.example.whattoeat.data.net.client

import android.util.Log
import com.example.whattoeat.data.net.encodeTo
import com.example.whattoeat.data.net.hack.ProxyRepository
import com.example.whattoeat.data.net.hack.UserAgentRepository
import com.example.whattoeat.data.net.parser.Parser
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.RecipeSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.helper.HttpConnection
import org.jsoup.nodes.Document


class RussianFoodComClient(
    private val proxyRepository: ProxyRepository,
//    = ProxyRepository()
    private val userAgentRepository: UserAgentRepository,
//    = UserAgentRepository()
    private val parser: Parser = Parser(proxyRepository, userAgentRepository)
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun searchRecipes(recipeSearch: RecipeSearch): Flow<Recipe> = flow {
        val searchDoc = fetchSearchPage(recipeSearch)
        val recipeLinks = parseSearchResults(searchDoc ?: return@flow)
        Log.d(TAG, "Found ${recipeLinks.size} links to parse")
        recipeLinks.forEach { link ->
            try {
                Log.d(TAG, "Parsing link: $link")
                val recipe = parser.parse(link)
                if (recipe != null) {
                    Log.d(TAG, "Emitting recipe: ${recipe.title}")
                    emit(recipe)
                } else {
                    Log.d(TAG, "Recipe is null for link: $link")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse $link", e)
            }
        }

        Log.d(TAG, "Finished parsing all links")
    }.flowOn(Dispatchers.IO)


    private suspend fun fetchSearchPage(recipeSearch: RecipeSearch): Document? {
        val url = buildSearchUrl(recipeSearch)
        Log.d(TAG, "fetchSearchPage(), url: $url")
        return withContext(Dispatchers.IO) {
            try {
                Jsoup.connect(url)
                    .userAgent(userAgentRepository.getRandomUserAgent())
                    .proxy(proxyRepository.getRandomProxy())
                    .referrer(REFERER)
                    .timeout(TIMEOUT)
                    .get()
            } catch (e: HttpStatusException) {
                Log.d(TAG, "code: ${e.statusCode}")
                null
            }
        }
    }

    private fun parseSearchResults(document: Document): List<String> {
        Log.d(TAG, "start to parse ${document.normalName()}")
        return document.getElementsByClass("in_seen")
            .mapNotNull { element ->
                element.selectFirst("a[href]")?.let { linkElement ->
                    val relativeLink = linkElement.attr("href")
                    if (relativeLink.startsWith(BASE_URL)) relativeLink
                    else BASE_URL + relativeLink
                }
            }
    }

    private fun buildSearchUrl(recipeSearch: RecipeSearch): String {
        val title = recipeSearch.title ?: Params.PARAM_2_DEFAULT_VALUE

        val inProducts = StringBuilder(Params.PARAM_6_DEFAULT_VALUE)
        recipeSearch.includedProducts?.map { products ->
            products.nameWithCount
        }?.forEach { inProducts.append(it).append(",") }

        val exProducts = StringBuilder(Params.PARAM_7_DEFAULT_VALUE)
        recipeSearch.excludedProducts?.map { products ->
            products.nameWithCount
        }?.forEach { exProducts.append(it).append(",") }

        val veganPart = if(recipeSearch.isVegan) {
            "${Params.PARAM_5}=${Params.PARAM_5_DEFAULT_VALUE}$"
        } else ""

        val inTextPart = if (recipeSearch.isSearchByDescription) {
            "${Params.PARAM_8}=${Params.PARAM_8_DEFAULT_VALUE}$"
        } else ""

        if (!recipeSearch.isAllProductsIncluded) {
            // TODO: сделать обработку фильтрации уже при выдаче пользователю. Если false, то надо будет кастомизировать пользовательский запрос
        }

        if (recipeSearch.isNeedVideo) {
            // TODO: сделать фильтрацию при выдаче пользователю
        }

        if (recipeSearch.isNeedSteps) {
            // TODO: сделать фильтрацию при выдаче пользователю
        }

//        TODO: также сейчас заметил, что в запросе не учитывается поиск по описанию, кухня, типы блюд и веганская еда... - допилить

        return "$URL_SEARCH? ${Params.PARAM_1}=${Params.PARAM_1_DEFAULT_VALUE}&${Params.PARAM_2}=${title.encodeTo(ENCODE)}&${Params.PARAM_3}=${recipeSearch.typeOfRecipe}&${Params.PARAM_4}=${recipeSearch.cuisine}&$veganPart${Params.PARAM_6}=${inProducts.toString().encodeTo(ENCODE)}&${Params.PARAM_7}=${exProducts.toString().encodeTo(ENCODE)}&$inTextPart${Params.PARAM_9}=${Params.PARAM_9_DEFAULT_VALUE}"
    }

    private object Params {
        const val PARAM_1 = "ssgrtype"
        const val PARAM_1_DEFAULT_VALUE = "bytype"
        const val PARAM_2 = "sskw_title"
        const val PARAM_2_DEFAULT_VALUE = ""
        const val PARAM_3 = "tag_tree%5B1%5D%5B%5D"
        const val PARAM_3_DEFAULT_VALUE = "0"
        const val PARAM_4 = "tag_tree%5B2%5D%5B%5D"
        const val PARAM_4_DEFAULT_VALUE = "0"
        const val PARAM_5 = "tag_tree%5B7%5D%5B216%5D"
        const val PARAM_5_DEFAULT_VALUE = ""
        const val PARAM_6 = "sskw_iplus"
        const val PARAM_6_DEFAULT_VALUE = ""
        const val PARAM_7 = "sskw_iminus"
        const val PARAM_7_DEFAULT_VALUE = ""
        const val PARAM_8 = "intext"
        const val PARAM_8_DEFAULT_VALUE = ""
        const val PARAM_9 = "submit"
        const val PARAM_9_DEFAULT_VALUE = ""
    }

    companion object {
        const val TIMEOUT = 10000
        const val REFERER = "https://www.russianfood.com/search/"
        private const val BASE_URL = "https://www.russianfood.com"
        private const val URL_SEARCH = "https://www.russianfood.com/search/simple/index.php"
        private const val ENCODE = "Windows-1251"

        private const val TAG = "RussianFoodComClientTAG"
    }
}