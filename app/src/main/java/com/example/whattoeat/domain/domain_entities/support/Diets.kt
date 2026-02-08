package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.SerialName

enum class Diets {
    @SerialName("dairy free")DAIRY_FREE,
    @SerialName("gluten free")GLUTEN_FREE, // Исключение глютена
    @SerialName("ketogenic")KETOGENIC, // Кетогенная диета: 55-80% жиров, 15-35% белков и менее 10% углеводов.
    @SerialName("vegetarian")VEGETARIAN, // Мясо или мясные субпродукты
    @SerialName("lacto vegetarian")LACTO_VEGETARIAN, // Вегетарианские, и ни один из ингредиентов не может содержать яйца.
    @SerialName("ovo vegetarian")OVO_VEGETARIAN, // Вегетарианские, и ни один из ингредиентов не может содержать молочные продукты.
    @SerialName("lacto ovo vegetarian")LACTO_OVO_VEGETARIAN,
    @SerialName("vegan")VEGAN, // Мясо или мясные субпродукты, а также яйца, молочные продукты и мёд
    @SerialName("pescetarian")PESCETARIAN, // Всё, кроме мяса и мясных субпродуктов — некоторые едят яйца и молочные продукты, другие — нет.
    @SerialName("paleo")PALEO, // https://www.google.com/search?q=paleo+diet
    @SerialName("primal")PRIMAL, // https://www.google.com/search?q=primal+diet
    @SerialName("low fodmap")LOW_FODMAP, // https://www.google.com/search?q=low+foodmap+diet
    @SerialName("whole 30")WHOLE_30, // https://www.google.com/search?q=whole+30+diet
}