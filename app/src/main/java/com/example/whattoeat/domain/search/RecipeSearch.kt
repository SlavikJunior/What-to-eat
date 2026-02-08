package com.example.whattoeat.domain.search

import com.example.whattoeat.domain.domain_entities.support.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import java.lang.reflect.Field
import kotlin.collections.forEach
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

sealed interface RecipeSearch {

    data class RecipeByIngredientsSearch(
        val ingredients: String = "", // Список ингредиентов, разделенных запятыми, которые должны содержаться в рецепте.
        val number: Int = 3, // Максимальное количество возвращаемых рецептов (от 1 до 100). По умолчанию — 10.
        val ranking: Int = 2, // Максимально использовать имеющиеся (1), минимизировать недостающие ингредиенты (2).
        val ignorePantry: Boolean = true // Игнорировать обычные продукты, такие как вода, соль, мука и т. д.?
    ): RecipeSearch

    data class RecipeComplexSearch(
        val query: String? = null, // Поисковый запрос (на естественном языке) для рецептов.
        val cuisines: List<Cuisines>? = null, // Кухня (или кухни)разделенные запятыми. Поддерживаемые: https://spoonacular.com/food-api/docs#Cuisines
        val diet: List<Diets>? = null, //  Диета (или диеты). Несколько диет, через (,) - «И». Несколько диет, через (|) «ИЛИ». Поддерживаемые: https://	spoonacular.com/food-api/docs#Diets
        val intolerances: List<String>? = null, // Непереносимости, через (,). Поддерживаемые: https://spoonacular.com/food-api/docs#Intolerances
        val includeIngredients: List<String>? = null, // Список ингредиентов, разделенных запятыми, которые следует/обязательно использовать в рецептах.
        val excludeIngredients: List<String>? = null, // Разделенный запятыми список ингредиентов, которые не должны содержаться в рецепте.
        val type: DishTypes? = null, // Тип рецепта. Поддерживаемые: https://spoonacular.com/food-api/docs#Meal-Types
        val instructionsRequired: Boolean? = null, // Необходимо ли наличие инструкций в рецептах.
        val addRecipeInformation: Boolean? = null, // Получить подробную информацию о возвращаемых рецептах.
        val addRecipeInstructions: Boolean? = null, // Получить проанализированные инструкции для каждого возвращаемого рецепта. Требуется addRecipeInformation  установленный в true.
        val addRecipeNutrition: Boolean? = null, // Получить информацию о пищевой ценности каждого возвращаемого рецепта.
        val maxReadyTime: Int? = null, // Максимальное время в минутах, которое должно потребоваться для приготовления данного блюда.
        val minServings: Int? = null, // Минимальное количество порций, на которое рассчитан рецепт.
        val sort: SortTypes? = null, // Сортировка (можно выбрать). Поддерживаемые: https://spoonacular.com/food-api/docs#Recipe-Sorting-Options
        val sortDirection: SortDirection? = null, // asc/desc
        val offset: Int? = null, // Количество результатов, которые пропускаются.
        val number: Int? = null // Количество ожидаемых результатов (1 - 0.01 балла стоит)
    ): RecipeSearch

    data class RecipeFullInformationSearch(
        @IdFromPath val id: Int, // Идентификатор рецепта. не парметр - часть в path!!!
        val includeNutrition: Boolean = true // Включите в описание рецепта информацию о пищевой ценности на одну порцию.
    ): RecipeSearch

    data class RecipeSimilarSearch(
        @IdFromPath val id: Int, // Идентификатор рецепта, для которого нужно найти похожие рецепты. не парметр - часть в path!!!
        val number: Int = 3 // Количество случайных рецептов, которые должны быть возвращены (от 1 до 100).
    ): RecipeSearch

    @JvmInline
    value class RecipeSummarySearch(
        @IdFromPath val id: Int // Идентификатор рецепта. не парметр - часть в path!!!
    ): RecipeSearch
}

fun RecipeSearch.toQueryMap(): Map<String, String> {
    val map = mutableMapOf<String, String>()

    this::class.memberProperties.forEach { property ->

        if (property.findAnnotation<IdFromPath>() != null) return@forEach

        val value = property.getter.call(this) ?: return@forEach
        val paramName = property.name

        when (value) {
            is Enum<*> -> {
                val serialName = value::class.java
                    .getField(value.name)
                    .getAnnotation(SerialName::class.java)

                map[paramName] = serialName?.value ?: value.name
            }

            is List<*> ->
                map[paramName] = value.joinToString(",")

            is Boolean, is Number, is String ->
                map[paramName] = value.toString()

            else ->
                throw IllegalArgumentException(
                    "Don't know how to serialize field '$paramName' from ${this::class.simpleName}"
                )
        }
    }

    return map
}


@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class IdFromPath