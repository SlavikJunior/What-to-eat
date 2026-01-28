package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.Serializable

// Значение, полученное из класса "step_n" из блока с классом "step_images_n списка шагов.
@Serializable
data class Step(
    val description: String?,
    val photo: String?
)