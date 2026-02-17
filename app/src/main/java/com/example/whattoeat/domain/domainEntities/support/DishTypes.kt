package com.example.whattoeat.domain.domainEntities.support

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


object DishTypesSerializer : EnumFallbackSerializer<DishTypes>(DishTypes.entries.toTypedArray(), DishTypes.UNKNOWN)

@Serializable(with = DishTypesSerializer::class)
enum class DishTypes(val text: String = "") {
    @SerialName("main course")MAIN_COURSE(text = "Главное блюдо"), // главное блюдо, включая гарниры
    @SerialName("side dish")SIDE_DISH(text = "Гарнир"), // гарнир
    @SerialName("main dish")MAIN_DISH, // главное блюдо, не включая гарниры
    @SerialName("dessert")DESSERT,
    @SerialName("appetizer")APPETIZER(text = "Закуска"), // закуска
    @SerialName("starter")STARTER,
    @SerialName("antipasto")ANTIPASTO,
    @SerialName("lunch")LUNCH(text = "Обед"),
    @SerialName("hor d'oeuvre")HOR_D_OEUVRE,
    @SerialName("antipasti")ANTIPASTI,
    @SerialName("morning meal")MORNING_MEAL,
    @SerialName("brunch")BRUNCH,
    @SerialName("dinner")DINNER,
    @SerialName("salad")SALAD(text = "Салат"),
    @SerialName("bread")BREAD,
    @SerialName("breakfast")BREAKFAST(text = "Завтрак"),
    @SerialName("soup")SOUP,
    @SerialName("beverage")BEVERAGE, // оффициальный напиток
    @SerialName("sauce")SAUCE,
    @SerialName("marinade")MARINADE,
    @SerialName("fingerfood")FINGERFOOD(text = "Fingerfood"), // закуски, которые можно есть руками
    @SerialName("snack")SNACK(text = "Снеки"), // перекус
    @SerialName("drink")DRINK(text = "Напитки"), // напиток
    UNKNOWN
}