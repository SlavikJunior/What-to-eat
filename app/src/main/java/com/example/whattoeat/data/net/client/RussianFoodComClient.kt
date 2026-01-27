package com.example.whattoeat.data.net.client

import com.example.whattoeat.data.net.encodeTo
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.CookingTime
import com.example.whattoeat.domain.domain_entities.support.Portions
import com.example.whattoeat.domain.domain_entities.support.Product
import com.example.whattoeat.domain.domain_entities.support.RecipeSearch
import com.example.whattoeat.domain.domain_entities.support.Step
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.regex.Matcher
import java.util.regex.Pattern


class RussianFoodComClient {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun searchRecipes(recipeSearch: RecipeSearch): Flow<Recipe> {
        return flow {
            val searchDoc = fetchSearchPage(recipeSearch)

            val recipeLinks = parseSearchResults(searchDoc)

            recipeLinks.forEach { link ->
                emit(link)
            }
        }
            .flatMapMerge { link ->
                flow {
                    parseRecipePage(link)?.let { recipe ->
                        emit(recipe)
                    }
                }
            }
            .flowOn(Dispatchers.IO)
    }


    private suspend fun fetchSearchPage(recipeSearch: RecipeSearch): Document {
        val url = buildSearchUrl(recipeSearch)
        return Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
            .referrer(REFERER)
            .timeout(TIMEOUT)
            .get()
    }

    private fun parseSearchResults(document: Document): List<String> {
        return document.getElementsByClass("in_ceen")
            .mapNotNull { element ->
                element.selectFirst("a[href]")?.let { linkElement ->
                    val relativeLink = linkElement.attr("href")
                    if (relativeLink.startsWith(BASE_URL)) relativeLink
                    else BASE_URL + relativeLink
                }
            }
    }

    private suspend fun parseRecipePage(url: String): Recipe? {
        return try {
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .referrer(REFERER)
                .timeout(TIMEOUT)
                .get()

            Recipe(
                title = extractTitle(doc),
                description = extractDescription(doc),
                image = extractImage(doc),
                fullDescription = extractFullDescription(doc),
                cookingTime = extractCookingTime(doc),
                portions = extractPortions(doc),
                products = extractProducts(doc),
                video = extractVideo(doc),
                steps = extractSteps(doc)
            )
        } catch (e: Exception) {
            throw e
        }
    }

    private fun extractTitle(doc: Document): String {
        var titleElement = doc.select("h1").first()
        if (titleElement == null)
            titleElement = doc.select(".title h3").first()
        if (titleElement == null)
            titleElement = doc.select(".center_block h2").first()
        return titleElement?.text() ?: "RecipeTitle"
    }
    private fun extractDescription(doc: Document): String {
        var descElement = doc.select(".announce p").first()
        if (descElement == null)
            descElement = doc.select(".recipe_text p").first()
        if (descElement == null)
            descElement = doc.select(".center_block p").first()
        return descElement?.text() ?: "RecipeDescription"
    }
    private fun extractImage(doc: Document): String? {
        var imgElement = doc.select(".foto_big img").first()
        if (imgElement == null)
            imgElement = doc.select(".image img").first()
        if (imgElement == null)
            imgElement = doc.select("img[src*=/dycontent/images_upl/]").first()

        return if (imgElement != null) {
            var imgUrl = if (imgElement.hasAttr("data-src"))
                imgElement.attr("data-src")
            else
                imgElement.attr("src")


            if (imgUrl.startsWith("//"))
                imgUrl = "https:$imgUrl"
            else if (imgUrl.startsWith("/"))
                imgUrl = "https://www.russianfood.com$imgUrl"

            imgUrl
        } else null
    }
    private fun extractFullDescription(doc: Document) = doc.getElementById("how")?.text()

    private fun extractCookingTime(doc: Document): CookingTime? {
        val timeElement = doc.select("div.sub_info div.el:has(i.ico_time)").first()
        return if (timeElement != null) {
            val totalTimeSpan = timeElement.select("span.hl").first()
            if (totalTimeSpan != null) {
                val timeText = totalTimeSpan.text()
                val bElement = totalTimeSpan.select("b").first()
                if (bElement != null) {
                    val timeValue: String = bElement.text()
                    val timeUnit: String = timeText.replace(timeValue, "").trim()
                    return CookingTime("$timeValue $timeUnit")
                } else null
            } else null
        } else null
    }
    private fun extractPortions(doc: Document): Portions? {
        val ingredientsTable = doc.getElementById("from")
        return if (ingredientsTable != null) {
            val portionElement = ingredientsTable.select("span.portion").first()
            if (portionElement != null) Portions(portionElement.text()) else null
        } else null
    }
    private fun extractProducts(doc: Document): List<Product>? {
        val ingredientsTable = doc.getElementById("from")
        return ingredientsTable?.let { table ->
            val ingredientElements = table.select("tr[class^=ingr_tr_] span")
            ingredientElements.map { element ->
                Product(
                    nameWithCount = element.text().trim()
                )
            }
        }
    }
    private fun extractVideo(doc: Document): String? {
        val ytLink = "https://www.youtube.com/watch?v="

        var videoElement = doc.getElementById("player0")
        if (videoElement == null) {
            videoElement = doc.select("div[id*=player]").first()
        }
        if (videoElement != null) {
            val html = videoElement.html()
            var pattern = Pattern.compile("videoId:\\s*'([^']+)'")
            var matcher = pattern.matcher(html)
            if (matcher.find()) {
                return matcher.group(1)
            }
            pattern = Pattern.compile("videoId\\s*=\\s*'([^']+)'")
            matcher = pattern.matcher(html)
            if (matcher.find()) {
                return matcher.group(1)
            }
        }
        val fullHtml = doc.html()
        val pattern: Pattern = Pattern.compile("videoId[=:]['\"]?([^'\"]+)['\"]?")
        val matcher: Matcher = pattern.matcher(fullHtml)
        val videoId = if (matcher.find())
            matcher.group(1)
        else null

        return videoId?.let { ytLink + it }
    }
    private fun extractSteps(doc: Document): List<Step>? {
        val stepsContainer = doc.getElementsByClass("step_images_n").firstOrNull()
        return stepsContainer?.let { container ->
            container.getElementsByClass("step_n").mapNotNull { stepElement ->
                val description = stepElement.selectFirst("p")?.text()?.trim()
                val photo = stepElement.selectFirst("img")?.let { img ->
                    var imgUrl = img.attr("src")
                    if (imgUrl.startsWith("//"))
                        imgUrl = "https:$imgUrl"
                    else if (imgUrl.startsWith("/"))
                        imgUrl = "https://www.russianfood.com$imgUrl"
                    imgUrl.takeIf { it.isNotEmpty() }
                }
                if (description != null || photo != null)
                    Step(description = description, photo = photo)
                else null
            }
        }?.takeIf { it.isNotEmpty() }
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
        }?.forEach { inProducts.append(it).append(",") }

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

        return """
            $URL_SEARCH?
            ${Params.PARAM_1}=${Params.PARAM_1_DEFAULT_VALUE}&
            ${Params.PARAM_2}=${title.encodeTo(ENCODE)}&
            ${Params.PARAM_3}=${Params.PARAM_3_DEFAULT_VALUE}&
            ${Params.PARAM_4}=${Params.PARAM_4_DEFAULT_VALUE}&
            ${Params.PARAM_5}=${Params.PARAM_5_DEFAULT_VALUE}&
            ${Params.PARAM_6}=${inProducts.toString().encodeTo(ENCODE)}&
            ${Params.PARAM_7}=${exProducts.toString().encodeTo(ENCODE)}&
            ${Params.PARAM_8}=${Params.PARAM_8_DEFAULT_VALUE}&
            ${Params.PARAM_9}=${Params.PARAM_9_DEFAULT_VALUE}
            """.trimIndent()
    }

    private object Params {
        const val PARAM_1 = "ssgrtype"
        const val PARAM_1_DEFAULT_VALUE = "bytype"
        const val PARAM_2 = "sskw_title"
        const val PARAM_2_DEFAULT_VALUE = ""
        const val PARAM_3 = "tag_tree[1][]"
        const val PARAM_3_DEFAULT_VALUE = "0"
        const val PARAM_4 = "tag_tree[2][]"
        const val PARAM_4_DEFAULT_VALUE = "0"
        const val PARAM_5 = "tag_tree[7][216]"
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
        private const val TIMEOUT = 10000
        private const val REFERER = "https://www.russianfood.com/search/"
        private const val BASE_URL = "https://www.russianfood.com"
        private const val URL_SEARCH = "https://www.russianfood.com/search/simple/index.php"
        private const val ENCODE = "Windows-1251"
    }
}