package com.example.whattoeat.domain.domain_entities.common

import kotlinx.serialization.Serializable

@Serializable
data class RecipeSummary(
    val id: Int,
    val title: String,
    val summary: String
): Recipe

@JvmInline
@Serializable
value class RecipeSummaryResult(val recipeSummary: RecipeSummary)