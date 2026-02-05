package com.example.whattoeat.presentation.ui.screens.custom_composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
    recipe: Recipe.RecipeComplex,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    var backgroundColor by remember { mutableStateOf(Color(0xC8ABD6FC)) }
    var titleColor by remember { mutableStateOf(Color(0xFF2B2B2B)) }

    Card(
        onClick = { onClick() },
        modifier = modifier
            .then(
                Modifier
                    .fillMaxWidth()
                    .then(if (!isImageLoaded) Modifier.shimmer() else Modifier)
                    .clickable(enabled = isImageLoaded) { onClick() }
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)) {
        Row(
            modifier = Modifier
                .height(120.dp)
                .padding(12.dp),
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
                    .size(90.dp)
                    .clip(RoundedCornerShape(18.dp)),
                onSuccess = { success ->
                    val bitmap = success.result.image.toBitmap(
                        success.result.image.width,
                        success.result.image.height
                    )

                    Palette.from(bitmap)
                        .maximumColorCount(16)
                        .generate { palette ->
                            palette?.let {
                                backgroundColor = Color(
                                    it.getLightMutedColor(0xFFEAF6FF.toInt())
                                )
                                titleColor = Color(
                                    it.getDarkVibrantColor(0xFF2B2B2B.toInt())
                                )
                            }
                        }
                    isImageLoaded = true
                }
            )

            Spacer(modifier = Modifier.width(width = 12.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = recipe.title,
                    color = titleColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }
    }
}