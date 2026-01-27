package com.example.whattoeat.data.net

import com.example.whattoeat.domain.domain_entities.support.Product
import com.example.whattoeat.domain.domain_entities.support.Step
import java.net.URLEncoder

fun String.encodeTo(encode: String) =
    URLEncoder.encode(this, encode)!!

inline fun <reified T> String.toListWithSeparator(separator: String): List<T>? {
    return when (T::class) {
        Product::class -> {
            this.split(separator)
                .filter { it.isNotBlank() }
                .map { Product(it.trim()) as T }
        }
        Step::class -> {
            this.split(separator)
                .filter { it.isNotBlank() }
                .mapNotNull { stepString ->
                    val parts = stepString.split("|")
                    val description = parts.getOrNull(0)?.trim()?.takeIf { it.isNotEmpty() }
                    val photo = parts.getOrNull(1)?.trim()?.takeIf { it.isNotEmpty() }

                    if (description != null || photo != null) {
                        Step(description, photo) as T
                    } else {
                        null
                    }
                }
        }
        else -> {
            throw IllegalArgumentException("Unsupported type: ${T::class}")
        }
    }.takeIf { it.isNotEmpty() }
}