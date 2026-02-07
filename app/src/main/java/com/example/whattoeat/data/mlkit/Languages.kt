package com.example.whattoeat.data.mlkit

enum class Languages(
    val bcp47Code: String
) {

    ENGLISH(bcp47Code = "en"),
    FRENCH(bcp47Code = "fr"),
    RUSSIAN(bcp47Code = "ru"),
    UNDEFINED(bcp47Code = "und")
}