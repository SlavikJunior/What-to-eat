package com.example.whattoeat.domain.domain_entities.common

// Бизнес-сущность избранного рецепта, по-факту, просто обёртка над рецептом.
@JvmInline
value class FavoriteRecipe(val recipe: Recipe)