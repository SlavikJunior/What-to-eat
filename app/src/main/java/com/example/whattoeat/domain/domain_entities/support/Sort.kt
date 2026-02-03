package com.example.whattoeat.domain.domain_entities.support

enum class SortTypes(val value: String) {

    POPULARITY(value = "popularity"),
    HEALTHINESS(value = "healthiness"),
    PRICE(value = "price"),
    TIME(value = "time"),
    MAX_USED_INGREDIENTS(value = "max-used-ingredients"),
    MIN_MISSING_INGREDIENTS(value = "min-missing-ingredients"),
    ENERGY(value = "energy"),
    CALORIES(value = "calories"),
    CARBS(value = "carbs"),
    TOTAL_FAT(value = "total-fat"),
    PROTEIN(value = "protein"),
    SUGAR(value = "sugar")
}

enum class SortDirection(val value: String) {

    ASC(value = "asc"),
    DESC(value = "desc")
}