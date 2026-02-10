package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.whattoeat.presentation.ui.screens.custom_composable.FilterBottomSheet
import com.example.whattoeat.presentation.ui.theme.Black
import com.example.whattoeat.presentation.ui.viewModels.RecipeListModelState
import com.example.whattoeat.presentation.ui.viewModels.RecipeListPageEvent
import com.example.whattoeat.presentation.ui.viewModels.RecipeListViewModel
import com.example.whattoeat.presentation.ui.viewModels.isDecreaseOffsetButtonEnabled
import com.example.whattoeat.presentation.ui.viewModels.isIncreaseOffsetButtonEnabled
import com.example.whattoeat.presentation.ui.viewModels.numberOfCurrentPage
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeList(
    navController: NavHostController,
    viewModel: RecipeListViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues()
) {
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    viewModel.reduce(
                        RecipeListPageEvent.IsFilterBottomSheetVisibleChange
                    )
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = null
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = {
                Text(stringResource(R.string.query_text_field_label))
            },
            value = uiState.value.filter.query.orEmpty(),
            singleLine = true,
            onValueChange = {
                viewModel.reduce(
                    RecipeListPageEvent.QueryChange(it)
                )
            }
        )

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            border = BorderStroke(
                width = 1.5.dp,
                color = Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = {
                viewModel.reduce(RecipeListPageEvent.SearchButtonClicked)
            }
        ) {
            Text(stringResource(R.string.search_button_text))
        }

        Spacer(Modifier.height(16.dp))

        if (uiState.value.isListShowing) {
            OffsetRecipeListNavigationRow(viewModel = viewModel)
        } else if (!uiState.value.isListShowing && uiState.value.modelState is RecipeListModelState.LoadingState) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color(
                            red = 75f,
                            green = 65f,
                            blue = 65f,
                            alpha = 0.15f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(uiState.value.recipes) { recipe ->
                RecipeCard(
                    navController = navController,
                    recipe = recipe,
                    viewModel = viewModel
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    FilterBottomSheet(viewModel)
}

@Composable
fun RecipeCard(
    navController: NavHostController,
    recipe: Recipe.RecipeComplexExt,
    viewModel: RecipeListViewModel,
    modifier: Modifier = Modifier
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    Card(
        onClick = { navController.navigate(RecipeDetailDataObject(recipeId = recipe.id)) },
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ID: ${recipe.id}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    IconButton(
                        onClick = {
                            viewModel.reduce(
                                event = RecipeListPageEvent.FavoriteRecipeChange(
                                    recipe = recipe
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OffsetRecipeListNavigationRow(
    viewModel: RecipeListViewModel
) {
    val uiState = viewModel.uiState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(
            onClick = {
                viewModel.reduce(RecipeListPageEvent.DecreaseOffsetChange)
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
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Surface(
                modifier = Modifier.size(24.dp),
                shape = RoundedCornerShape(4.dp),
                shadowElevation = 4.dp
            ) {
                Text(
                    text = uiState.value.totalResults.toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )
            }
        }

        IconButton(
            onClick = {
                viewModel.reduce(RecipeListPageEvent.IncreaseOffsetChange)
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