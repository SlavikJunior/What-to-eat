package com.example.whattoeat.domain.models.support

import kotlinx.serialization.Serializable

@Serializable
data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)