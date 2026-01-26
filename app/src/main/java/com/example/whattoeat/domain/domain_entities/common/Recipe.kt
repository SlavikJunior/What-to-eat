package com.example.whattoeat.domain.domain_entities.common

import com.example.whattoeat.domain.domain_entities.support.CookingTime
import com.example.whattoeat.domain.domain_entities.support.Step
import com.example.whattoeat.domain.domain_entities.support.Portions
import com.example.whattoeat.domain.domain_entities.support.Product

// Бизнес-сущность рецепта, будет отправляться на ui в таком виде,
// а репозитории должны уметь обернуть в неё сырые данные из data слоя.
data class Recipe(
    val title: String,
    val description: String,
    val image: String? = null,
    val fullDescription: String? = null,
    val cookingTime: CookingTime? = null,
    val portions: Portions? = null,
    val products: List<Product>? = null,
    val video: String? = null,
    val steps: List<Step>? = null
)