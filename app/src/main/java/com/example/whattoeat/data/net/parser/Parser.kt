package com.example.whattoeat.data.net.parser

import android.util.Log
import com.example.whattoeat.data.net.client.RussianFoodComClient.Companion.REFERER
import com.example.whattoeat.data.net.client.RussianFoodComClient.Companion.TIMEOUT
import com.example.whattoeat.data.net.hack.ProxyRepository
import com.example.whattoeat.data.net.hack.UserAgentRepository
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.domain.domain_entities.support.CookingTime
import com.example.whattoeat.domain.domain_entities.support.Portions
import com.example.whattoeat.domain.domain_entities.support.Product
import com.example.whattoeat.domain.domain_entities.support.Step
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.regex.Matcher
import java.util.regex.Pattern

class Parser(
    private val proxyRepository: ProxyRepository,
    private val userAgentRepository: UserAgentRepository
) {

    suspend fun parse(url: String): Recipe? {
        Log.d("ParserTAG", "Starting parse for url: $url")
        return try {
            withContext(Dispatchers.IO) {
                try {
                    val doc = Jsoup.connect(url)
                        .userAgent(userAgentRepository.getRandomUserAgent())
                        .proxy(proxyRepository.getRandomProxy())
                        .referrer(REFERER)
                        .timeout(TIMEOUT)
                        .get()

                    Log.d("ParserTAG", "Document fetched for: $url")

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
                    ).also {
                        Log.d("ParserTAG", "Recipe created: ${it.title}")
                    }
                } catch (e: Exception) {
                    Log.e("ParserTAG", "Error parsing $url: ${e.message}", e)
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("ParserTAG", "Outer exception for $url: ${e.message}", e)
            null
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
}