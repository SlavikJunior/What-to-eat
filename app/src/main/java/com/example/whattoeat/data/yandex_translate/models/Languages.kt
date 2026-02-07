package com.example.whattoeat.data.yandex_translate.models

enum class Languages(
    val bcp47Code: String
) {

    ENGLISH(bcp47Code = "en"),
    FRENCH(bcp47Code = "fr"),
    RUSSIAN(bcp47Code = "ru"),
    UNDEFINED(bcp47Code = "und")
}