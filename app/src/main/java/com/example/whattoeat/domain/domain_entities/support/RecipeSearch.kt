package com.example.whattoeat.domain.domain_entities.support

// Описание пользовательского поиска рецепта
data class RecipeSearch(
    val title: String? = null,
    val includedProducts: List<Product>? = null,
    val excludedProducts: List<Product>? = null,
    val isAllProductsIncluded: Boolean = false,
    val isNeedVideo: Boolean = true,
    val isNeedSteps: Boolean = false,
)