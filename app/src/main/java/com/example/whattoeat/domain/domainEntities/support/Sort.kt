package com.example.whattoeat.domain.domainEntities.support

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object SortTypesSerializer : EnumFallbackSerializer<SortTypes>(SortTypes.entries.toTypedArray(), SortTypes.POPULARITY)

@Serializable(with = SortTypesSerializer::class)
enum class SortTypes(val text: String = "") {

    @SerialName(value = "popularity") POPULARITY(text = "Популярность"),
    @SerialName(value = "healthiness") HEALTHINESS(text = "Здоровье"),
    @SerialName(value = "price") PRICE(text = "Цена"),
    @SerialName(value = "time") TIME(text = "Время"),
    @SerialName(value = "max-used-ingredients") MAX_USED_INGREDIENTS(text = "Максимум своих"),
    @SerialName(value = "min-missing-ingredients") MIN_MISSING_INGREDIENTS(text = "Минимум новых"),
    @SerialName(value = "energy") ENERGY(text = "Энергия"),
    @SerialName(value = "calories") CALORIES(text = "Калории"),
    @SerialName(value = "carbs") CARBS(text = "Углеводы"),
    @SerialName(value = "total-fat") TOTAL_FAT(text = "Жиры"),
    @SerialName(value = "protein") PROTEIN(text = "Белки"),
    @SerialName(value = "sugar") SUGAR(text = "Сахар"),
    UNKNOWN
}

object SortDirectionSerializer : EnumFallbackSerializer<SortDirection>(SortDirection.entries.toTypedArray(), SortDirection.ASC)

@Serializable(with = SortDirectionSerializer::class)
enum class SortDirection(val text: String = "") {

    @SerialName(value = "asc") ASC(text = "Возрастание"),
    @SerialName(value = "desc") DESC(text = "Убывание")
}