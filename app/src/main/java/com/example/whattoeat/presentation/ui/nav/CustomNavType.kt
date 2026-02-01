package com.example.whattoeat.presentation.ui.nav

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.example.whattoeat.domain.domain_entities.common.Recipe
import kotlinx.serialization.json.Json

object CustomNavType {

    val RecipeNavType = object : NavType<Recipe>(isNullableAllowed = false) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: Recipe,
        ) = bundle.putString(key, Json.encodeToString(value))

        override fun get(
            bundle: SavedState,
            key: String,
        ) =
            bundle.getString(key)?.let { Json.decodeFromString<Recipe>(it) }

        override fun parseValue(value: String) =
            Json.decodeFromString<Recipe>(value)

        override fun serializeAsValue(value: Recipe) =
            Json.encodeToString(value)

    }
}