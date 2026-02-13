package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object SortTypesSerializer : EnumFallbackSerializer<SortTypes>(SortTypes.entries.toTypedArray(), SortTypes.POPULARITY)

@Serializable(with = SortTypesSerializer::class)
enum class SortTypes() {

    @SerialName(value = "popularity") POPULARITY,
    @SerialName(value = "healthiness") HEALTHINESS,
    @SerialName(value = "price") PRICE,
    @SerialName(value = "time") TIME,
    @SerialName(value = "max-used-ingredients") MAX_USED_INGREDIENTS,
    @SerialName(value = "min-missing-ingredients") MIN_MISSING_INGREDIENTS,
    @SerialName(value = "energy") ENERGY,
    @SerialName(value = "calories") CALORIES,
    @SerialName(value = "carbs") CARBS,
    @SerialName(value = "total-fat") TOTAL_FAT,
    @SerialName(value = "protein") PROTEIN,
    @SerialName(value = "sugar") SUGAR,
    UNKNOWN
}

object SortDirectionSerializer : EnumFallbackSerializer<SortDirection>(SortDirection.entries.toTypedArray(), SortDirection.ASC)

@Serializable(with = SortDirectionSerializer::class)
enum class SortDirection() {

    @SerialName(value = "asc") ASC,
    @SerialName(value = "desc") DESC
}