package com.example.whattoeat.domain.domain_entities.support

// Значение, полученное из класса "sub_info" из шапки рецепта,
// возможно полученное значение будет полным временем готовки,
// а возможно полное и время, нужно от пользователя.
@JvmInline
value class CookingTime(val value: String)