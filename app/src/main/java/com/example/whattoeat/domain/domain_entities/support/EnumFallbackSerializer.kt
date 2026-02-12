package com.example.whattoeat.domain.domain_entities.support

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

abstract class EnumFallbackSerializer<T : Enum<T>>(
    private val values: Array<T>,
    private val fallback: T
) : KSerializer<T> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("EnumFallbackSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        val serialName = value::class.java.getField(value.name)
            .getAnnotation(SerialName::class.java)?.value ?: value.name
        encoder.encodeString(serialName)
    }

    override fun deserialize(decoder: Decoder): T {
        val decodedValue = decoder.decodeString()
        return values.find { item ->
            val serialName = item::class.java.getField(item.name)
                .getAnnotation(SerialName::class.java)?.value
            serialName.equals(decodedValue, ignoreCase = true) ||
                    item.name.equals(decodedValue, ignoreCase = true)
        } ?: fallback
    }
}