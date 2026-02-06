package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.SerialName

enum class DishTypes {
    @SerialName("main course")MAIN_COURSE, // второе блюдо
    @SerialName("side dish") SIDE_DISH, // гарнир
    @SerialName("dessert")DESSERT,
    @SerialName("appetizer")APPETIZER, // закуска
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
}