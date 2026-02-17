package com.example.whattoeat.domain.domainEntities.support

import kotlinx.serialization.Serializable

@Serializable
data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)