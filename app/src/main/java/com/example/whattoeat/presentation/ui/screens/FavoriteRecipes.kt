package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import coil3.compose.AsyncImage
import com.example.whattoeat.R
import com.example.whattoeat.domain.models.common.Recipe
import com.example.whattoeat.presentation.ui.nav.Screen
import com.example.whattoeat.presentation.ui.viewModels.FavoriteRecipesPageEvent
import com.example.whattoeat.presentation.ui.viewModels.FavoriteRecipesPageStatus
import com.example.whattoeat.presentation.ui.viewModels.FavoriteRecipesViewModel
import com.valentinilk.shimmer.shimmer

@Composable
fun FavoriteRecipes(
    backStack: NavBackStack<Screen>,
    paddingValues: PaddingValues,
    viewModel: FavoriteRecipesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.reduce(FavoriteRecipesPageEvent.LoadRecipes)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        when (uiState.status) {
            FavoriteRecipesPageStatus.LOADING -> {
                if (!uiState.isListShowing) {
                    CircularProgressIndicator()
                }
            }

            FavoriteRecipesPageStatus.ERROR -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(R.string.error_loading_favorites_label))
                    Button(onClick = { viewModel.reduce(FavoriteRecipesPageEvent.LoadRecipes) }) {
                        Text(text = stringResource(R.string.retry_loading_favorites_label))
                    }
                }
            }

            FavoriteRecipesPageStatus.SUCCESS -> {
                if (uiState.recipes.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.empty_loading_favorites_label),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.recipes, key = { it.recipe.id }) { recipe ->
                            FavoriteRecipeCard(
                                backStack = backStack,
                                recipe = recipe
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteRecipeCard(
    backStack: NavBackStack<Screen>,
    recipe: Recipe.RecipeComplexExt,
    modifier: Modifier = Modifier
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    Card(
        onClick = { backStack.add(Screen.RecipeDetailScreen(recipe.id)) },
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .then(if (!isImageLoaded) Modifier.shimmer() else Modifier),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = recipe.image,
                contentDescription = stringResource(
                    R.string.recipe_card_image_content_description,
                    recipe.title
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
                    .clip(RoundedCornerShape(16.dp)),
                onSuccess = {
                    isImageLoaded = true
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = recipe.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
            }
        }
    }
}