package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Cuisines {
    @SerialName("African") AFRICAN,
    @SerialName("Asian") ASIAN,
    @SerialName("American") AMERICAN,
    @SerialName("British") BRITISH,
    @SerialName("Cajun") CAJUN,
    @SerialName("Caribbean") CARIBBEAN,
    @SerialName("Chinese") CHINESE,
    @SerialName("Eastern European") EASTERN_EUROPEAN,
    @SerialName("European") EUROPEAN,
    @SerialName("French") FRENCH,
    @SerialName("German") GERMAN,
    @SerialName("Greek") GREEK,
    @SerialName("Indian") INDIAN,
    @SerialName("Irish") IRISH,
    @SerialName("Italian") ITALIAN,
    @SerialName("Japanese") JAPANESE,
    @SerialName("Jewish") JEWISH,
    @SerialName("Korean") KOREAN,
    @SerialName("Latin American") LATIN_AMERICAN,
    @SerialName("Mediterranean") MEDITERRANEAN,
    @SerialName("Mexican") MEXICAN,
    @SerialName("Middle Eastern") MIDDLE_EASTERN,
    @SerialName("Nordic") NORDIC,
    @SerialName("Southern") SOUTHERN,
    @SerialName("Spanish") SPANISH,
    @SerialName("Thai") THAI,
    @SerialName("Vietnamese") VIETNAMESE
}