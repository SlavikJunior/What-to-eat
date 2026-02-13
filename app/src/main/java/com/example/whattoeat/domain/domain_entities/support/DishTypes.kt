package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


object DishTypesSerializer : EnumFallbackSerializer<DishTypes>(DishTypes.entries.toTypedArray(), DishTypes.UNKNOWN)

@Serializable(with = DishTypesSerializer::class)
enum class DishTypes {
    @SerialName("main course")MAIN_COURSE, // второе блюдо
    @SerialName("side dish")SIDE_DISH, // гарнир
    @SerialName("main dish")MAIN_DISH, // гарнир
    @SerialName("dessert")DESSERT,
    @SerialName("appetizer")APPETIZER, // закуска
    @SerialName("starter")STARTER,
    @SerialName("antipasto")ANTIPASTO,
    @SerialName("lunch")LUNCH,
    @SerialName("hor d'oeuvre")HOR_D_OEUVRE,
    @SerialName("antipasti")ANTIPASTI,
    @SerialName("morning meal")MORNING_MEAL,
    @SerialName("brunch")BRUNCH,
    @SerialName("dinner")DINNER,
    @SerialName("salad")SALAD,
    @SerialName("bread")BREAD,
    @SerialName("breakfast")BREAKFAST,
    @SerialName("soup")SOUP,
    @SerialName("beverage")BEVERAGE, // оффициальный напиток
    @SerialName("sauce")SAUCE,
    @SerialName("marinade")MARINADE,
    @SerialName("fingerfood")FINGERFOOD, // закуски, которые можно есть руками
    @SerialName("snack")SNACK, // перекус
    @SerialName("drink")DRINK, // напиток
    UNKNOWN
}