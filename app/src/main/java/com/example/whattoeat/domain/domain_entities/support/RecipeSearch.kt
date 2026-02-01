package com.example.whattoeat.domain.domain_entities.support

// Описание пользовательского поиска рецепта
data class RecipeSearch(
    val title: String? = null,
    val typeOfRecipe: Int = 27,
    val cuisine: Int = 103,
    val includedProducts: List<Product>? = null,
    val excludedProducts: List<Product>? = null,
    val isAllProductsIncluded: Boolean = false,
    val isSearchByDescription: Boolean = false,
    val isVegan: Boolean = false,
    val isNeedVideo: Boolean = true,
    val isNeedSteps: Boolean = false,
)