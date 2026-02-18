package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.whattoeat.R
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.domain.domainEntities.support.AnalyzedInstruction
import com.example.whattoeat.domain.domainEntities.support.Step
import com.example.whattoeat.presentation.ui.nav.RecipeDetailDataObject
import com.example.whattoeat.presentation.ui.viewModels.RecipeDetailModelState
import com.example.whattoeat.presentation.ui.viewModels.RecipeDetailPageEvent
import com.example.whattoeat.presentation.ui.viewModels.RecipeDetailViewModel
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetail(
    navController: NavHostController,
    paddingValues: PaddingValues,
    dataObject: RecipeDetailDataObject,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val recipe = uiState.recipe

    LaunchedEffect(dataObject.recipeId) {
        viewModel.reduce(RecipeDetailPageEvent.LoadRecipe(dataObject.recipeId))
    }

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            if (uiState.modelState is RecipeDetailModelState.DefaultState && recipe != null) {
                FloatingActionButton(
                    onClick = { viewModel.reduce(RecipeDetailPageEvent.FavoriteCurrentRecipeChange) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .size(64.dp)
                ) {
                    Icon(
                        imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (recipe.isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (recipe.isFavorite) Color.Red else LocalContentColor.current
                    )

                }
            }
        }
    ) { scaffoldPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            when (uiState.modelState) {
                RecipeDetailModelState.LoadingState -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                RecipeDetailModelState.DefaultState -> {
                    recipe?.let { item ->
                        RecipeDetailContent(
                            recipe = item,
                            similarRecipes = uiState.similarRecipes,
                            onSimilarClick = { id ->
                                navController.navigate(RecipeDetailDataObject(id))
                            },
                            onSimilarFavorite = { similar ->
                                viewModel.reduce(
                                    RecipeDetailPageEvent.FavoriteSimilarRecipeChange(
                                        similar
                                    )
                                )
                            },
                            onTranslateClick = { viewModel.reduce(event = RecipeDetailPageEvent.TranslateRecipe) },
                            isTranslated = uiState.isTranslated
                        )
                    }
                }

                is RecipeDetailModelState.ErrorState -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.error_state_label))
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeDetailContent(
    recipe: Recipe.RecipeFullInformationExt,
    similarRecipes: List<Recipe.RecipeSimilarExt>,
    onSimilarClick: (Int) -> Unit,
    onSimilarFavorite: (Recipe.RecipeSimilarExt) -> Unit,
    onTranslateClick: () -> Unit,
    isTranslated: Boolean,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }

        item {
            Text(
                recipe.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Chip(stringResource(R.string.ready_in_minutes_title, recipe.readyInMinutes).lowercase())
                Chip(stringResource(R.string.servings_title, recipe.servings).lowercase())
                Chip(stringResource(R.string.agregate_likes_title, recipe.aggregateLikes).lowercase())
                Chip(stringResource(R.string.health_title, recipe.healthScore.toInt()))
                Chip(stringResource(R.string.score_title, recipe.spoonacularScore.toInt()))
            }
        }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Chip(stringResource(if (recipe.vegetarian) R.string.vegetarian_title_true else R.string.vegetarian_title_false).lowercase())
                Chip(stringResource(if (recipe.vegan) R.string.vegan_title_true else R.string.vegan_title_false).lowercase())
                Chip(stringResource(if (recipe.glutenFree) R.string.gluten_free_title_true else R.string.gluten_free_title_false).lowercase())
                Chip(stringResource(if (recipe.dairyFree) R.string.dairy_free_title_true else R.string.dairy_free_title_false).lowercase())
                Chip(stringResource(if (recipe.veryHealthy) R.string.very_healthy_title_true else R.string.very_healthy_title_false).lowercase())
            }
        }

        item {
            Text(
                recipe.summary.replace(Regex("<[^>]*>"), ""),
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
        }

        item {
            Button(
                onClick = onTranslateClick,
                enabled = !isTranslated
            ) {
                Text(
                    text = "Translate Instruction",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        item {
            InstructionsSection(
                analyzedInstructions = recipe.analyzedInstructions,
                fallbackText = recipe.instructions
            )
        }

        if (similarRecipes.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.similar_recipes_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(
                        horizontal = 4.dp,
                        vertical = 8.dp
                    ) // Немного паддинга для теней карточек
                ) {
                    items(similarRecipes) { similar ->
                        SimilarRecipeCard(
                            recipe = similar,
                            onClick = { onSimilarClick(similar.id) },
                            onFavoriteClick = { onSimilarFavorite(similar) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InstructionsSection(
    analyzedInstructions: List<AnalyzedInstruction>,
    fallbackText: String
) {
    Text(
        text = stringResource(R.string.instructions_section_title),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )

    Spacer(Modifier.height(12.dp))

    if (analyzedInstructions.isNotEmpty()) {
        analyzedInstructions.first().let { block ->
            if (block.name.isNotBlank()) {
                Text(
                    block.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
            }

            block.steps.forEach { step ->
                StepItem(step)
                Spacer(Modifier.height(12.dp))
            }
        }
    } else {
        Text(
            fallbackText,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp
        )
    }
}

@Composable
private fun StepItem(step: Step) {
    Column {
        Row(verticalAlignment = Alignment.Top) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(step.number.toString(), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.width(12.dp))

            Text(
                step.step,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (step.ingredients.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                step.ingredients.forEach {
                    Chip(it.name)
                }
            }
        }
    }
}

@Composable
private fun Chip(text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun SimilarRecipeCard(
    recipe: Recipe.RecipeSimilarExt,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var imageLoaded by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier = modifier
            .width(160.dp)
            .height(200.dp)
            .then(if (!imageLoaded) Modifier.shimmer() else Modifier),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                onSuccess = { imageLoaded = true }
            )

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = recipe.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (recipe.isFavorite) MaterialTheme.colorScheme.primary else LocalContentColor.current
                        )
                    }
                }
            }
        }
    }
}