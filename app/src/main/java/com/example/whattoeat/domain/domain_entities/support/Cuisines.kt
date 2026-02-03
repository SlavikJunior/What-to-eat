package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.SerialName

enum class Cuisines {
    @SerialName("african")AFRICAN,
    @SerialName("asian")ASIAN,
    @SerialName("american")AMERICAN,
    @SerialName("british")BRITISH,
    @SerialName("chinese")CHINESE,
    @SerialName("eastern_european")EASTERN_EUROPEAN,
    @SerialName("european")EUROPEAN,
    @SerialName("french")FRENCH,
    @SerialName("german")GERMAN,
    @SerialName("greek")GREEK,
    @SerialName("indian")INDIAN,
    @SerialName("italian")ITALIAN,
    @SerialName("japanese")JAPANESE,
    @SerialName("korean")KOREAN,
    @SerialName("latin_american")LATIN_AMERICAN,
    @SerialName("mexican")MEXICAN,
    @SerialName("nordic")NORDIC,
    @SerialName("spanish")SPANISH,
    @SerialName("thai")THAI,
    @SerialName("vietnamese")VIETNAMESE,
    @SerialName("unknown")UNKNOWN
}