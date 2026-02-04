package com.example.whattoeat.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

object TypeConverters {

    @TypeConverter
    fun fromListToJsonString(list: List<*>) = Json.encodeToString(list)

    @TypeConverter
    fun <T> fromJsonStringToList(jsonString: String) =
        Json.decodeFromString<List<T>>(jsonString)
}