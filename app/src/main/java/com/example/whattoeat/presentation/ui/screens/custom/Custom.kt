package com.example.whattoeat.presentation.ui.screens.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.whattoeat.R
import com.example.whattoeat.domain.domain_entities.support.Cuisines
import com.example.whattoeat.domain.domain_entities.support.Diets
import com.example.whattoeat.domain.domain_entities.support.DishTypes
import com.example.whattoeat.presentation.ui.viewModels.RecipeListModel
import com.example.whattoeat.presentation.ui.viewModels.RecipeListPageEvent
import com.example.whattoeat.presentation.ui.viewModels.RecipeListViewModel
import com.example.whattoeat.presentation.ui.viewModels.SearchType

@Composable
fun FilterBottomSheet(
    viewModel: RecipeListViewModel
) {
    val uiState = viewModel.uiState.collectAsState()

    FilterBottomSheetContent(
        uiState = uiState,
        onFilterBottomSheetVisibleChange = { viewModel.reduce(event = RecipeListPageEvent.IsFilterBottomSheetVisibleChange) },
        onIncludedProductsChange = {
            viewModel.reduce(
                event = RecipeListPageEvent.IncludedProductsChange(
                    it.includedProducts
                )
            )
        },
        onExcludedProductsChange = {
            viewModel.reduce(
                event = RecipeListPageEvent.ExcludedProductsChange(
                    it.excludedProducts
                )
            )
        },
        onCuisineChange = {
            viewModel.reduce(
                event = RecipeListPageEvent.CuisineChange(
                    it.cuisine
                )
            )
        },
        onDishTypeChange = {
            viewModel.reduce(
                event = RecipeListPageEvent.DishTypeChange(
                    it.type
                )
            )
        },
        onDietChange = {
            viewModel.reduce(
                event = RecipeListPageEvent.DietChange(
                    it.diet
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheetContent(
    uiState: State<RecipeListModel>,
    onFilterBottomSheetVisibleChange: (event: RecipeListPageEvent.IsFilterBottomSheetVisibleChange) -> Unit,
    onIncludedProductsChange: (event: RecipeListPageEvent.IncludedProductsChange) -> Unit,
    onExcludedProductsChange: (event: RecipeListPageEvent.ExcludedProductsChange) -> Unit,
    onCuisineChange: (event: RecipeListPageEvent.CuisineChange) -> Unit,
    onDishTypeChange: (event: RecipeListPageEvent.DishTypeChange) -> Unit,
    onDietChange: (event: RecipeListPageEvent.DietChange) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    if (uiState.value.isFilterBottomSheetVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { onFilterBottomSheetVisibleChange(RecipeListPageEvent.IsFilterBottomSheetVisibleChange) }
        ) {

            if (uiState.value.searchType == SearchType.COMPLEX_SEARCH) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProductsBlock(
                        uiState = uiState,
                        onIncludedProductsChange = onIncludedProductsChange,
                        onExcludedProductsChange = onExcludedProductsChange
                    )

                    CuisinesBlock(
                        uiState = uiState,
                        onCuisineChange = { onCuisineChange(RecipeListPageEvent.CuisineChange(it.cuisine)) }
                    )

                    DishTypesBlock(
                        uiState = uiState,
                        onDishTypeChange = onDishTypeChange
                    )

                    DietsBlock(
                        uiState = uiState,
                        onDietChange = onDietChange
                    )
                }
            } else {

            }
        }
    }
}

@Composable
private fun DietsBlock(
    uiState: State<RecipeListModel>,
    onDietChange: (event: RecipeListPageEvent.DietChange) -> Unit
) {
    HorizontalDivider()

    Text(text = "Диеты")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            ToggleButtonChip(
                enum = Diets.GLUTEN_FREE,
                checked = uiState.value.filter.diet?.contains(Diets.GLUTEN_FREE) ?: false,
                onCheckedChange = { checked -> onDietChange }
            )
        }

        item {
            ToggleButtonChip(
                enum = Diets.VEGETARIAN,
                checked = uiState.value.filter.diet?.contains(Diets.VEGETARIAN) ?: false,
                onCheckedChange = { checked -> onDietChange }
            )
        }

        item {
            ToggleButtonChip(
                enum = Diets.VEGAN,
                checked = uiState.value.filter.diet?.contains(Diets.VEGAN) ?: false,
                onCheckedChange = { checked -> onDietChange }
            )
        }

        item {
            ToggleButtonChip(
                enum = Diets.KETOGENIC,
                checked = uiState.value.filter.diet?.contains(Diets.KETOGENIC) ?: false,
                onCheckedChange = { checked -> onDietChange }
            )
        }

        item {
            ToggleButtonChip(
                enum = Diets.PALEO,
                checked = uiState.value.filter.diet?.contains(Diets.PALEO) ?: false,
                onCheckedChange = { checked -> onDietChange }
            )
        }

        item {
            ToggleButtonChip(
                enum = Diets.PRIMAL,
                checked = uiState.value.filter.diet?.contains(Diets.PRIMAL) ?: false,
                onCheckedChange = { checked -> onDietChange }
            )
        }
    }
}

@Composable
private fun DishTypesBlock(
    uiState: State<RecipeListModel>,
    onDishTypeChange: (event: RecipeListPageEvent.DishTypeChange) -> Unit
) {
    HorizontalDivider()

    Text(text = "Типы блюд")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            ToggleButtonChip(
                enum = DishTypes.MAIN_DISH,
                checked = uiState.value.filter.type == DishTypes.MAIN_DISH,
                onCheckedChange = { checked ->
                    onDishTypeChange(
                        RecipeListPageEvent.DishTypeChange(
                            DishTypes.MAIN_DISH
                        )
                    )
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = DishTypes.SIDE_DISH,
                checked = uiState.value.filter.type == DishTypes.SIDE_DISH,
                onCheckedChange = { checked ->
                    onDishTypeChange(
                        RecipeListPageEvent.DishTypeChange(
                            DishTypes.SIDE_DISH
                        )
                    )
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = DishTypes.MAIN_COURSE,
                checked = uiState.value.filter.type == DishTypes.MAIN_COURSE,
                onCheckedChange = { checked ->
                    onDishTypeChange(
                        RecipeListPageEvent.DishTypeChange(
                            DishTypes.MAIN_COURSE
                        )
                    )
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = DishTypes.APPETIZER,
                checked = uiState.value.filter.type == DishTypes.APPETIZER,
                onCheckedChange = { checked ->
                    onDishTypeChange(
                        RecipeListPageEvent.DishTypeChange(
                            DishTypes.APPETIZER
                        )
                    )
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = DishTypes.LUNCH,
                checked = uiState.value.filter.type == DishTypes.LUNCH,
                onCheckedChange = { checked ->
                    onDishTypeChange(
                        RecipeListPageEvent.DishTypeChange(
                            DishTypes.LUNCH
                        )
                    )
                }
            )
        }
        
        item {
            ToggleButtonChip(
                enum = DishTypes.SALAD,
                checked = uiState.value.filter.type == DishTypes.SALAD,
                onCheckedChange = { checked ->
                    onDishTypeChange(
                        RecipeListPageEvent.DishTypeChange(
                            DishTypes.SALAD
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun CuisinesBlock(
    uiState: State<RecipeListModel>,
    onCuisineChange: (event: RecipeListPageEvent.CuisineChange) -> Unit
) {
    HorizontalDivider()

    Text(text = "Кухни")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            ToggleButtonChip(
                enum = Cuisines.AMERICAN,
                checked = uiState.value.filter.cuisines?.contains(Cuisines.AMERICAN) ?: false,
                onCheckedChange = { checked ->
                    onCuisineChange(
                        RecipeListPageEvent.CuisineChange(
                            Cuisines.AMERICAN
                        )
                    )
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = Cuisines.EASTERN_EUROPEAN,
                checked = uiState.value.filter.cuisines?.contains(Cuisines.EASTERN_EUROPEAN)
                    ?: false,
                onCheckedChange = { checked ->
                    onCuisineChange(RecipeListPageEvent.CuisineChange(Cuisines.EASTERN_EUROPEAN))
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = Cuisines.EUROPEAN,
                checked = uiState.value.filter.cuisines?.contains(Cuisines.EUROPEAN) ?: false,
                onCheckedChange = { checked ->
                    onCuisineChange(RecipeListPageEvent.CuisineChange(Cuisines.EUROPEAN))
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = Cuisines.FRENCH,
                checked = uiState.value.filter.cuisines?.contains(Cuisines.FRENCH) ?: false,
                onCheckedChange = { checked ->
                    onCuisineChange(RecipeListPageEvent.CuisineChange(Cuisines.FRENCH))
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = Cuisines.GERMAN,
                checked = uiState.value.filter.cuisines?.contains(Cuisines.GERMAN) ?: false,
                onCheckedChange = { checked ->
                    onCuisineChange(
                        RecipeListPageEvent.CuisineChange(
                            Cuisines.GERMAN
                        )
                    )
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = Cuisines.ITALIAN,
                checked = uiState.value.filter.cuisines?.contains(Cuisines.ITALIAN) ?: false,
                onCheckedChange = { checked ->
                    onCuisineChange(
                        RecipeListPageEvent.CuisineChange(
                            Cuisines.ITALIAN
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun ToggleButtonChip(
    enum: Enum<*>,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    OutlinedIconToggleButton(
        checked = checked,
        onCheckedChange = { onCheckedChange(it) },
        modifier = Modifier
            .size(64.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Text(
                text = enum.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProductsBlock(
    uiState: State<RecipeListModel>,
    onIncludedProductsChange: (event: RecipeListPageEvent.IncludedProductsChange) -> Unit,
    onExcludedProductsChange: (event: RecipeListPageEvent.ExcludedProductsChange) -> Unit,
) {
    OutlinedTextField(
        label = {
            Text(stringResource(R.string.inclusive_products_text_field_label))
        },
        value = uiState.value.filter.includedProducts.orEmpty(),
        onValueChange = { newValue ->
            onIncludedProductsChange(
                RecipeListPageEvent.IncludedProductsChange(
                    includedProducts = newValue
                )
            )
        }
    )

    OutlinedTextField(
        label = {
            Text(stringResource(R.string.exclusive_products_text_field_label))
        },
        value = uiState.value.filter.excludedProducts.orEmpty(),
        onValueChange = { newValue ->
            onExcludedProductsChange(
                RecipeListPageEvent.ExcludedProductsChange(
                    excludedProducts = newValue
                )
            )
        }
    )

}