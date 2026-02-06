package com.example.whattoeat.presentation.ui.screens.custom_composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import coil3.compose.AsyncImage
import coil3.toBitmap
import com.example.whattoeat.R
import com.example.whattoeat.domain.domain_entities.common.Recipe
import com.example.whattoeat.presentation.ui.view_models.RecipeListPageEvent
import com.example.whattoeat.presentation.ui.view_models.RecipeListViewModel
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    viewModel: RecipeListViewModel
) {
    val uiState = viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    if (uiState.value.isFilterBottomSheetVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                viewModel.reduce(
                    RecipeListPageEvent.IsFilterBottomSheetVisibleChange
                )
            }
        ) {
            Column {
                OutlinedTextField(
                    label = {
                        Text(stringResource(R.string.inclusive_products_text_field_label))
                    },
                    value = uiState.value.filter.includedProducts ?: "",
                    onValueChange = {
                        viewModel.reduce(
                            RecipeListPageEvent.IncludedProductsChange(it)
                        )
                    }
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    label = {
                        Text(stringResource(R.string.exclusive_products_text_field_label))
                    },
                    value = uiState.value.filter.excludedProducts ?: "",
                    onValueChange = {
                        viewModel.reduce(
                            RecipeListPageEvent.ExcludedProductsChange(it)
                        )
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}


@Composable
fun RecipeCard(
    recipe: Recipe.RecipeComplexExt,
    viewModel: RecipeListViewModel,
    modifier: Modifier = Modifier
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    Card(
        onClick = { viewModel.reduce(event = TODO()) },
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
            // Изображение рецепта
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

            // Информация о рецепте
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Заголовок
                Text(
                    text = recipe.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                // Дополнительная информация
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
fun OffsetNavigationRow(
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
        // Кнопка назад
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

        // Кнопка вперед
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