package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.SerialName

enum class Cuisines {
    @SerialName("African")AFRICAN,
    @SerialName("Asian")ASIAN,
    @SerialName("American")AMERICAN,
    @SerialName("British")BRITISH,
    @SerialName("Chinese")CHINESE,
    @SerialName("Eastern European")EASTERN_EUROPEAN,
    @SerialName("European")EUROPEAN,
    @SerialName("French")FRENCH,
    @SerialName("German")GERMAN,
    @SerialName("Greek")GREEK,
    @SerialName("Indian")INDIAN,
    @SerialName("Italian")ITALIAN,
    @SerialName("Japanese")JAPANESE,
    @SerialName("Korean")KOREAN,
    @SerialName("Latin American")LATIN_AMERICAN,
    @SerialName("Mexican")MEXICAN,
    @SerialName("Nordic")NORDIC,
    @SerialName("Spanish")SPANISH,
    @SerialName("Thai")THAI,
    @SerialName("Vietnamese")VIETNAMESE,
    @SerialName("unknown")UNKNOWN
}