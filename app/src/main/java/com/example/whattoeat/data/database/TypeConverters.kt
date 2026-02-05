package com.example.whattoeat.data.database

import androidx.room.TypeConverter
import com.example.whattoeat.domain.domain_entities.support.AnalyzedInstruction
import com.example.whattoeat.domain.domain_entities.support.Cuisines
import com.example.whattoeat.domain.domain_entities.support.Diets
import com.example.whattoeat.domain.domain_entities.support.DishTypes
import com.example.whattoeat.domain.domain_entities.support.Ingredient
import kotlinx.serialization.json.Json

object TypeConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromIngredientList(ingredients: List<Ingredient>): String =
        json.encodeToString(ingredients)

    @TypeConverter
    fun toIngredientList(jsonString: String): List<Ingredient> =
        json.decodeFromString(jsonString)

    @TypeConverter
    fun fromCuisinesList(cuisines: List<Cuisines>): String =
        json.encodeToString(cuisines.map { it.name })

    @TypeConverter
    fun toCuisinesList(jsonString: String): List<Cuisines> = json.decodeFromString<List<String>>(jsonString).map { Cuisines.valueOf(it) }

    @TypeConverter
    fun fromDietsList(diets: List<Diets>): String = json.encodeToString(diets.map { it.name })

    @TypeConverter
    fun toDietsList(jsonString: String): List<Diets> = json.decodeFromString<List<String>>(jsonString).map { Diets.valueOf(it) }

    @TypeConverter
    fun fromDishTypesList(dishTypes: List<DishTypes>): String = json.encodeToString(dishTypes.map { it.name })

    @TypeConverter
    fun toDishTypesList(jsonString: String): List<DishTypes> = json.decodeFromString<List<String>>(jsonString).map { DishTypes.valueOf(it) }

    @TypeConverter
    fun fromStringList(list: List<String>): String = json.encodeToString(list)

    @TypeConverter
    fun toStringList(jsonString: String): List<String> = json.decodeFromString(jsonString)

    @TypeConverter
    fun fromAnalyzedInstructionList(instructions: List<AnalyzedInstruction>): String =
        json.encodeToString(instructions)

    @TypeConverter
    fun toAnalyzedInstructionList(jsonString: String): List<AnalyzedInstruction> =
        json.decodeFromString(jsonString)
}