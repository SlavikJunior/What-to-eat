package com.example.whattoeat.domain.domain_entities.support

enum class Diets {
    GLUTEN_FREE, // Исключение глютена
    KETOGENIC, // Кетогенная диета: 55-80% жиров, 15-35% белков и менее 10% углеводов.
    VEGETARIAN, // Мясо или мясные субпродукты
    LACTO_VEGETARIAN, // Вегетарианские, и ни один из ингредиентов не может содержать яйца.
    OVO_VEGETARIAN, // Вегетарианские, и ни один из ингредиентов не может содержать молочные продукты.
    VEGAN, // Мясо или мясные субпродукты, а также яйца, молочные продукты и мёд
    PESCETARIAN, // Всё, кроме мяса и мясных субпродуктов — некоторые едят яйца и молочные продукты, другие — нет.
    PALEO, // https://www.google.com/search?q=paleo+diet
    PRIMAL, // https://www.google.com/search?q=primal+diet
    LOW_FODMAP, // https://www.google.com/search?q=low+foodmap+diet
    WHOLE_30 // https://www.google.com/search?q=whole+30+diet
}