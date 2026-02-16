package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object CuisinesSerializer : EnumFallbackSerializer<Cuisines>(Cuisines.entries.toTypedArray(), Cuisines.UNKNOWN)

@Serializable(with = CuisinesSerializer::class)
enum class Cuisines(val text: String = "") {
    

    @SerialName("African") AFRICAN(text = "AFRICAN"),
    @SerialName("American") AMERICAN(text = "Американская"),
    @SerialName("Asian") ASIAN(text = "ASIAN"),
    @SerialName("British") BRITISH(text = "BRITISH"),
    @SerialName("Cajun") CAJUN(text = "CAJUN"),
    @SerialName("Caribbean") CARIBBEAN(text = "CARIBBEAN"),
    @SerialName("Chinese") CHINESE(text = "CHINESE"),
    @SerialName("Creole") CREOLE(text = "CREOLE"),
    @SerialName("Eastern European") EASTERN_EUROPEAN(text = "Вост Европейская"),
    @SerialName("European") EUROPEAN(text = "Европейская"),
    @SerialName("French") FRENCH(text = "Французская"),
    @SerialName("German") GERMAN(text = "Немецкая"),
    @SerialName("Greek") GREEK(text = "GREEK"),
    @SerialName("Indian") INDIAN(text = "INDIAN"),
    @SerialName("Irish") IRISH(text = "IRISH"),
    @SerialName("Italian") ITALIAN(text = "Итальянская"),
    @SerialName("Japanese") JAPANESE(text = "JAPANESE"),
    @SerialName("Jewish") JEWISH(text = "JEWISH"),
    @SerialName("Korean") KOREAN(text = "KOREAN"),
    @SerialName("Latin American") LATIN_AMERICAN(text = "LATIN_AMERICAN"),
    @SerialName("Mediterranean") MEDITERRANEAN(text = "MEDITERRANEAN"),
    @SerialName("Mexican") MEXICAN(text = "MEXICAN"),
    @SerialName("Middle Eastern") MIDDLE_EASTERN(text = "MIDDLE_EASTERN"),
    @SerialName("Nordic") NORDIC(text = "NORDIC"),
    @SerialName("Southern") SOUTHERN(text = "SOUTHERN"),
    @SerialName("Spanish") SPANISH(text = "SPANISH"),
    @SerialName("Thai") THAI(text = "THAI"),
    @SerialName("Vietnamese") VIETNAMESE(text = "VIETNAMESE"),
    UNKNOWN;
}