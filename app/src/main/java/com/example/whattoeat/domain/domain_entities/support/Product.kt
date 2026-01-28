package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.Serializable

// Значение, полученное из класса "ingr_tr_n" из списка ингридиентов,
// возможно полученное значение будет без пропорций, т.к. приходит это всё в одной строке.
@Serializable
@JvmInline
value class Product(
    val nameWithCount: String
)