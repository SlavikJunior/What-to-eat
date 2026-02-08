package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.presentation.ui.nav.RecipeDetailDataObject
import com.example.whattoeat.presentation.ui.screens.custom_composable.OffsetRecipeListNavigationRow
import com.example.whattoeat.presentation.ui.screens.custom_composable.SimilarRecipeCard
import com.example.whattoeat.presentation.ui.view_models.RecipeDetailModelState
import com.example.whattoeat.presentation.ui.view_models.RecipeDetailPageEvent
import com.example.whattoeat.presentation.ui.view_models.RecipeDetailViewModel
import com.example.whattoeat.presentation.ui.view_models.RecipeListPageEvent
import com.example.whattoeat.presentation.ui.view_models.RecipeListViewModel
import com.example.whattoeat.presentation.ui.view_models.isDecreaseOffsetButtonEnabled
import com.example.whattoeat.presentation.ui.view_models.isIncreaseOffsetButtonEnabled
import com.example.whattoeat.presentation.ui.view_models.numberOfCurrentPage
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetail(
    navController: NavHostController,
    dataObject: RecipeDetailDataObject,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val recipe = uiState.recipe?.recipe
    val isFavorite = uiState.recipe?.isFavorite ?: false

    LaunchedEffect(Unit) {
        viewModel.reduce(RecipeDetailPageEvent.LoadRecipe(dataObject.recipeId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Рецепт") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.reduce(RecipeDetailPageEvent.FavoriteCurrentRecipeChange) }) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Избранное",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else LocalContentColor.current
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (uiState.modelState) {
            RecipeDetailModelState.LoadingState -> LoadingContent(padding)
            RecipeDetailModelState.DefaultState -> {
                if (recipe != null) {
                    Content(
                        recipe = recipe,
                        isFavorite = isFavorite,
                        similarRecipes = uiState.similarRecipes,
                        paddingValues = padding,
                        onSimilarRecipeClick = { similarId ->
                            navController.navigate(RecipeDetailDataObject(similarId))
                        },
                        onSimilarFavoriteClick = { similar ->
                            viewModel.reduce(RecipeDetailPageEvent.FavoriteSimilarRecipeChange(similar))
                        },
                        viewModel = viewModel
                    )
                }
            }
            is RecipeDetailModelState.ErrorState -> ErrorContent(padding, "Error state :/")
        }
    }
}
@Composable
private fun LoadingContent(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(padding: PaddingValues, message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_error),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun Content(
    recipe: Recipe.RecipeFullInformation,
    isFavorite: Boolean,
    similarRecipes: List<Recipe.RecipeSimilarExt>,
    paddingValues: PaddingValues,
    onSimilarRecipeClick: (Int) -> Unit,
    onSimilarFavoriteClick: (Recipe.RecipeSimilarExt) -> Unit,
    viewModel: RecipeDetailViewModel
) {
    var imageLoaded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Фото
        item {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .then(if (!imageLoaded) Modifier.shimmer() else Modifier),
                onSuccess = { imageLoaded = true }
            )
        }

        // Заголовок
        item {
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp
            )
        }

        // Метрики
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RecipeChip(painterResource(R.drawable.ic_timer), "${recipe.readyInMinutes} мин")
                RecipeChip(painterResource(R.drawable.ic_people), "${recipe.servings} порций")
                RecipeChip(Icons.Default.Favorite, "${recipe.aggregateLikes}", MaterialTheme.colorScheme.primary)
                if (recipe.healthScore > 0) {
                    RecipeChip(painterResource(R.drawable.ic_heart), "Здоровье: ${recipe.healthScore.toInt()}", MaterialTheme.colorScheme.tertiary)
                }
            }
        }

        item { HorizontalDivider() }

        // Особенности
        if (recipe.vegetarian || recipe.vegan || recipe.glutenFree || recipe.dairyFree || recipe.veryHealthy) {
            item {
                Text("Особенности", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (recipe.vegetarian) DietChip("Вегетарианское")
                    if (recipe.vegan) DietChip("Веганское")
                    if (recipe.glutenFree) DietChip("Без глютена")
                    if (recipe.dairyFree) DietChip("Без лактозы")
                    if (recipe.veryHealthy) DietChip("Очень полезно")
                }
            }
        }

        // Описание
        item {
            Text("Описание", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(
                text = recipe.summary.replace(Regex("<[^>]*>"), ""),
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
        }

        // === ПОХОЖИЕ РЕЦЕПТЫ ===
        item {
            SimilarRecipesSection(
                similarRecipes = similarRecipes,
                viewModel = viewModel,
                onSimilarRecipeClick = onSimilarRecipeClick,
                onSimilarFavoriteClick = onSimilarFavoriteClick
            )
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun SimilarRecipesSection(
    similarRecipes: List<Recipe.RecipeSimilarExt>,
    viewModel: RecipeDetailViewModel,
    onSimilarRecipeClick: (Int) -> Unit,
    onSimilarFavoriteClick: (Recipe.RecipeSimilarExt) -> Unit
) {
    Column {
        Text(
            text = "Похожие рецепты",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        // Кнопки навигации (как в RecipeList)
        OffsetRecipeDetailNavigationRow(viewModel = viewModel)   // можно потом сделать отдельную для similar

        Spacer(Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(items = similarRecipes) { similar ->
                SimilarRecipeCard(
                    recipe = similar,
                    onClick = { onSimilarRecipeClick(similar.id) },
                    onFavoriteClick = { onSimilarFavoriteClick(similar) }
                )
            }
        }
    }
}

@Composable
fun OffsetRecipeDetailNavigationRow(
    viewModel: RecipeDetailViewModel
) {
    val uiState = viewModel.uiState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // Кнопка назад
        IconButton(
            onClick = {
                viewModel.reduce(RecipeDetailPageEvent.DecreaseOffsetChange)
            },
            enabled = uiState.value.isDecreaseOffsetButtonEnabled()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = stringResource(R.string.previous_page)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(24.dp),
                shape = RoundedCornerShape(4.dp),
                shadowElevation = 4.dp
            ) {
                Text(
                    text = uiState.value.numberOfCurrentPage().toString(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Surface(
                modifier = Modifier.size(24.dp),
                shape = RoundedCornerShape(4.dp),
                shadowElevation = 4.dp
            ) {
                Text(
                    text = uiState.value.totalResults.toString()
                )
            }
        }

        // Кнопка вперед
        IconButton(
            onClick = {
                viewModel.reduce(RecipeDetailPageEvent.IncreaseOffsetChange)
            },
            enabled = uiState.value.isIncreaseOffsetButtonEnabled()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.next_page)
            )
        }
    }
}


@Composable
private fun RecipeChip(
    painter: Painter,
    text: String,
    tint: Color = LocalContentColor.current
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = tint
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RecipeChip(
    imageVector: ImageVector,
    text: String,
    tint: Color = LocalContentColor.current
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = tint
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DietChip(text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun SimilarRecipeCard(
    recipe: Recipe.RecipeSimilarExt,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var imageLoaded by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier = modifier
            .width(164.dp)
            .height(210.dp)
            .then(if (!imageLoaded) Modifier.shimmer() else Modifier),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                onSuccess = { imageLoaded = true }
            )

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
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