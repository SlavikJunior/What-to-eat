package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.whattoeat.R
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.presentation.ui.nav.RecipeDetailDataObject
import com.example.whattoeat.presentation.ui.nav.UsersRecipeDetailDataObject
import com.example.whattoeat.presentation.ui.screens.custom.Chip
import com.example.whattoeat.presentation.ui.viewModels.RecipeDetailModelState
import com.example.whattoeat.presentation.ui.viewModels.RecipeDetailPageEvent
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipeDetailModelState
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipeDetailPageEvent
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipeDetailViewModel

@Composable
fun UsersRecipeDetail(
    navController: NavHostController,
    paddingValues: PaddingValues,
    dataObject: UsersRecipeDetailDataObject,
    viewModel: UsersRecipeDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val recipe = uiState.recipe

    LaunchedEffect(dataObject.recipeId) {
        viewModel.reduce(UsersRecipeDetailPageEvent.LoadRecipe(dataObject.recipeId))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when (uiState.modelState) {
            UsersRecipeDetailModelState.LoadingState -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            UsersRecipeDetailModelState.DefaultState -> {
                recipe?.let { item ->
                    UsersRecipeDetailContent(
                        recipe = item,
                    )
                }
            }

            is UsersRecipeDetailModelState.ErrorState -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.error_state_label))
                }
            }
        }
    }
}

@Preview
@Composable
private fun UsersRecipeDetailContentPreview() =
    UsersRecipeDetailContent(
        recipe = Recipe.RecipeByUser(
            id = 12,
            title = "Рецепт блинов",
            readyInMinutes = 12,
            servings = 14,
            ingredients = "Мука, яйца, молоко ...........",
            notes = "Рецепт лучших блинов"
        )
    )

@Composable
private fun UsersRecipeDetailContent(
    recipe: Recipe.RecipeByUser
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                recipe.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (recipe.readyInMinutes != null && recipe.readyInMinutes > 0)
                    Chip(
                        stringResource(
                            R.string.ready_in_minutes_title,
                            recipe.readyInMinutes
                        ).lowercase()
                    )

                if (recipe.servings != null && recipe.servings > 0)
                    Chip(stringResource(R.string.servings_title, recipe.servings).lowercase())
            }
        }

        if (recipe.notes != null && recipe.notes.isNotBlank()) {
            item {
                Text(
                    text = "notes",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = recipe.notes,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp
                )
            }
        }

        if (recipe.ingredients != null && recipe.ingredients.isNotBlank()) {
            item {
                Text(
                    text = "ingredients",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = recipe.ingredients,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp
                )

            }
        }
    }
}