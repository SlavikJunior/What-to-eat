package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object DietsSerializer : EnumFallbackSerializer<Diets>(Diets.entries.toTypedArray(), Diets.UNKNOWN)

@Serializable(with = DietsSerializer::class)
enum class Diets(val text: String = "") {
    @SerialName("dairy free")DAIRY_FREE(text = "DAIRY_FREE"),
    @SerialName("pescatarian")PESCATARIAN(text = "PESCATARIAN"),
    @SerialName("paleolithic")PALEOLITHIC(text = "PALEOLITHIC"),
    @SerialName("gluten free")GLUTEN_FREE(text = "Без глютена"), // Исключение глютена
    @SerialName("ketogenic")KETOGENIC(text = "Кетогенная"), // Кетогенная диета: 55-80% жиров, 15-35% белков и менее 10% углеводов.
    @SerialName("vegetarian")VEGETARIAN(text = "Вегетарианская"), // Мясо или мясные субпродукты
    @SerialName("lacto vegetarian")LACTO_VEGETARIAN(text = "LACTO_VEGETARIAN"), // Вегетарианские, и ни один из ингредиентов не может содержать яйца.
    @SerialName("ovo vegetarian")OVO_VEGETARIAN(text = "OVO_VEGETARIAN"), // Вегетарианские, и ни один из ингредиентов не может содержать молочные продукты.
    @SerialName("lacto ovo vegetarian")LACTO_OVO_VEGETARIAN(text = "LACTO_OVO_VEGETARIAN"),
    @SerialName("vegan")VEGAN(text = "Веганская"), // Мясо или мясные субпродукты, а также яйца, молочные продукты и мёд
    @SerialName("pescetarian")PESCETARIAN(text = "PESCETARIAN"), // Всё, кроме мяса и мясных субпродуктов — некоторые едят яйца и молочные продукты, другие — нет.
    @SerialName("paleo")PALEO(text = "Палео"), // https://www.google.com/search?q=paleo+diet
    @SerialName("primal")PRIMAL(text = "Праймал"), // https://www.google.com/search?q=primal+diet
    @SerialName("low fodmap")LOW_FODMAP(text = "LOW_FODMAP"), // https://www.google.com/search?q=low+foodmap+diet
    @SerialName("whole 30")WHOLE_30(text = "WHOLE_30"), // https://www.google.com/search?q=whole+30+diet
    UNKNOWN
}