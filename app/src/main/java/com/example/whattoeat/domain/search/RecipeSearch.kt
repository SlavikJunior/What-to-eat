package com.example.whattoeat.domain.search

import com.example.whattoeat.domain.domain_entities.support.*
import kotlinx.serialization.Serializable

sealed interface RecipeSearch {

    @Serializable
    data class RecipeByIngredientsSearch(
        val ingredients: String, // Список ингредиентов, разделенных запятыми, которые должны содержаться в рецепте.
        val number: Int, // Максимальное количество возвращаемых рецептов (от 1 до 100). По умолчанию — 10.
        val ranking: Int, // Максимально использовать имеющиеся (1), минимизировать недостающие ингредиенты (2).
        val ignorePantry: Boolean // Игнорировать обычные продукты, такие как вода, соль, мука и т. д.?
    ): RecipeSearch

    @Serializable
    data class RecipeComplexSearch(
        val query: String? = null, // Поисковый запрос (на естественном языке) для рецептов.
        val cuisines: Cuisines? = null, // Кухня (или кухни)разделенные запятыми. Поддерживаемые: https://spoonacular.com/food-api/docs#Cuisines
        val diet: Diets? = null, //  Диета (или диеты). Несколько диет, через (,) - «И». Несколько диет, через (|) «ИЛИ». Поддерживаемые: https://	spoonacular.com/food-api/docs#Diets
        val intolerances: String? = null, // Непереносимости, через (,). Поддерживаемые: https://spoonacular.com/food-api/docs#Intolerances
        val includeIngredients: String? = null, // Список ингредиентов, разделенных запятыми, которые следует/обязательно использовать в рецептах.
        val excludeIngredients: String? = null, // Разделенный запятыми список ингредиентов, которые не должны содержаться в рецепте.
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

    @Serializable
    data class RecipeInformationSearch(
        val id: Int, // Идентификатор рецепта. не парметр - часть в path!!!
        val includeNutrition: Boolean // Включите в описание рецепта информацию о пищевой ценности на одну порцию.
    ): RecipeSearch

    @Serializable
    data class RecipeSimilarSearch(
        val id: Int, // Идентификатор рецепта, для которого нужно найти похожие рецепты. не парметр - часть в path!!!
        val number: Int // Количество случайных рецептов, которые должны быть возвращены (от 1 до 100).
    ): RecipeSearch

    @JvmInline
    @Serializable
    value class RecipeSummarySearch(
        val id: Int // Идентификатор рецепта. не парметр - часть в path!!!
    ): RecipeSearch
}