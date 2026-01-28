package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.Serializable

// Значение, полученное из класса "portions" из заголовка ингридиентов.
@Serializable
@JvmInline
value class Portions(val value: String)