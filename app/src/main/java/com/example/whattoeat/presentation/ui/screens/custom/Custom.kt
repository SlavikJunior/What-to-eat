package com.example.whattoeat.presentation.ui.screens.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.whattoeat.domain.domainEntities.support.Cuisines
import com.example.whattoeat.domain.domainEntities.support.Diets
import com.example.whattoeat.domain.domainEntities.support.DishTypes
import com.example.whattoeat.domain.domainEntities.support.SortDirection
import com.example.whattoeat.domain.domainEntities.support.SortTypes
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
        },
        onSortTypeChange = {
            viewModel.reduce(
                event = RecipeListPageEvent.SortTypeChange(
                    it.sortType
                )
            )
        },
        onSortDirectionChange = {
            viewModel.reduce(
                event = RecipeListPageEvent.SortDirectionChange(
                    it.sortDirection
                )
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheetContent(
    uiState: State<RecipeListModel>,
    onFilterBottomSheetVisibleChange: (event: RecipeListPageEvent.IsFilterBottomSheetVisibleChange) -> Unit,
    onCuisineChange: (event: RecipeListPageEvent.CuisineChange) -> Unit,
    onDishTypeChange: (event: RecipeListPageEvent.DishTypeChange) -> Unit,
    onDietChange: (event: RecipeListPageEvent.DietChange) -> Unit,
    onSortTypeChange: (event: RecipeListPageEvent.SortTypeChange) -> Unit,
    onSortDirectionChange: (event: RecipeListPageEvent.SortDirectionChange) -> Unit,
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
                    SortBlock(
                        uiState = uiState,
                        onSortTypeChange = onSortTypeChange,
                        onSortDirectionChange = onSortDirectionChange
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
                checked = uiState.value.filter.diet.contains(Diets.GLUTEN_FREE),
                onCheckedChange = { checked -> onDietChange(
                    RecipeListPageEvent.DietChange(
                        diet = Diets.GLUTEN_FREE
                    ))
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = Diets.VEGETARIAN,
                checked = uiState.value.filter.diet.contains(Diets.VEGETARIAN),
                onCheckedChange = { checked -> onDietChange(
                    RecipeListPageEvent.DietChange(
                        diet = Diets.VEGETARIAN
                    ))
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = Diets.VEGAN,
                checked = uiState.value.filter.diet.contains(Diets.VEGAN),
                onCheckedChange = { checked -> onDietChange(
                    RecipeListPageEvent.DietChange(
                        diet = Diets.VEGAN
                    ))
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = Diets.KETOGENIC,
                checked = uiState.value.filter.diet.contains(Diets.KETOGENIC),
                onCheckedChange = { checked -> onDietChange(
                    RecipeListPageEvent.DietChange(
                        diet = Diets.KETOGENIC
                    ))
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = Diets.PALEO,
                checked = uiState.value.filter.diet.contains(Diets.PALEO),
                onCheckedChange = { checked -> onDietChange(
                    RecipeListPageEvent.DietChange(
                        diet = Diets.PALEO
                    ))
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = Diets.PRIMAL,
                checked = uiState.value.filter.diet.contains(Diets.PRIMAL),
                onCheckedChange = { checked -> onDietChange(
                    RecipeListPageEvent.DietChange(
                        diet = Diets.PRIMAL
                    ))
                }
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
                enum = DishTypes.BREAKFAST,
                checked = uiState.value.filter.type == DishTypes.BREAKFAST,
                onCheckedChange = { checked ->
                    onDishTypeChange(
                        RecipeListPageEvent.DishTypeChange(
                            DishTypes.BREAKFAST
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

        item {
            ToggleButtonChip(
                enum = DishTypes.DRINK,
                checked = uiState.value.filter.type == DishTypes.DRINK,
                onCheckedChange = { checked ->
                    onDishTypeChange(
                        RecipeListPageEvent.DishTypeChange(
                            DishTypes.DRINK
                        )
                    )
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = DishTypes.SNACK,
                checked = uiState.value.filter.type == DishTypes.SNACK,
                onCheckedChange = { checked ->
                    onDishTypeChange(
                        RecipeListPageEvent.DishTypeChange(
                            DishTypes.SNACK
                        )
                    )
                }
            )
        }

        item {
            ToggleButtonChip(
                enum = DishTypes.FINGERFOOD,
                checked = uiState.value.filter.type == DishTypes.FINGERFOOD,
                onCheckedChange = { checked ->
                    onDishTypeChange(
                        RecipeListPageEvent.DishTypeChange(
                            DishTypes.FINGERFOOD
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

//@Preview
//@Composable
//private fun ToggleButtonChipPreview() =
//    ToggleButtonChip(
//        enum = Cuisines.EASTERN_EUROPEAN,
//        checked = false,
//        onCheckedChange = {}
//    )

@Composable
private fun ToggleButtonChip(
    enum: Enum<*>,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val name = when (enum) {
        is DishTypes -> enum.text
        is Cuisines -> enum.text
        is Diets -> enum.text
        is SortTypes -> enum.text
        is SortDirection -> enum.text
        else -> ""
    }

    OutlinedIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = Modifier.size(64.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = if (checked) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = name.lowercase(),
                    modifier = Modifier.padding(horizontal = 6.dp),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun SortBlock(
    uiState: State<RecipeListModel>,
    onSortTypeChange: (event: RecipeListPageEvent.SortTypeChange) -> Unit,
    onSortDirectionChange: (event: RecipeListPageEvent.SortDirectionChange) -> Unit,
) {
    HorizontalDivider()

    Text(text = "Сортировка")

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ToggleButtonChip(
                enum = SortDirection.ASC,
                checked = uiState.value.filter.sortDirection == SortDirection.ASC,
                onCheckedChange = { checked ->
                    onSortDirectionChange(RecipeListPageEvent.SortDirectionChange(sortDirection = SortDirection.ASC))
                }
            )

            ToggleButtonChip(
                enum = SortDirection.DESC,
                checked = uiState.value.filter.sortDirection == SortDirection.DESC,
                onCheckedChange = { checked ->
                    onSortDirectionChange(RecipeListPageEvent.SortDirectionChange(sortDirection = SortDirection.DESC))
                }
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            item {
//                ToggleButtonChip(
//                    enum = SortTypes.MAX_USED_INGREDIENTS,
//                    checked = uiState.value.filter.sort == SortTypes.MAX_USED_INGREDIENTS,
//                    onCheckedChange = { checked ->
//                        onSortTypeChange(RecipeListPageEvent.SortTypeChange(sortType = SortTypes.MAX_USED_INGREDIENTS))
//                    }
//                )
//            }
//
//            item {
//                ToggleButtonChip(
//                    enum = SortTypes.MIN_MISSING_INGREDIENTS,
//                    checked = uiState.value.filter.sort == SortTypes.MIN_MISSING_INGREDIENTS,
//                    onCheckedChange = { checked ->
//                        onSortTypeChange(RecipeListPageEvent.SortTypeChange(sortType = SortTypes.MIN_MISSING_INGREDIENTS))
//                    }
//                )
//            }

            item {
                ToggleButtonChip(
                    enum = SortTypes.TIME,
                    checked = uiState.value.filter.sort == SortTypes.TIME,
                    onCheckedChange = { checked ->
                        onSortTypeChange(RecipeListPageEvent.SortTypeChange(sortType = SortTypes.TIME))
                    }
                )
            }

            item {
                ToggleButtonChip(
                    enum = SortTypes.ENERGY,
                    checked = uiState.value.filter.sort == SortTypes.ENERGY,
                    onCheckedChange = { checked ->
                        onSortTypeChange(RecipeListPageEvent.SortTypeChange(sortType = SortTypes.ENERGY))
                    }
                )
            }

            item {
                ToggleButtonChip(
                    enum = SortTypes.CALORIES,
                    checked = uiState.value.filter.sort == SortTypes.CALORIES,
                    onCheckedChange = { checked ->
                        onSortTypeChange(RecipeListPageEvent.SortTypeChange(sortType = SortTypes.CALORIES))
                    }
                )
            }

            item {
                ToggleButtonChip(
                    enum = SortTypes.SUGAR,
                    checked = uiState.value.filter.sort == SortTypes.SUGAR,
                    onCheckedChange = { checked ->
                        onSortTypeChange(RecipeListPageEvent.SortTypeChange(sortType = SortTypes.SUGAR))
                    }
                )
            }

            item {
                ToggleButtonChip(
                    enum = SortTypes.POPULARITY,
                    checked = uiState.value.filter.sort == SortTypes.POPULARITY,
                    onCheckedChange = { checked ->
                        onSortTypeChange(RecipeListPageEvent.SortTypeChange(sortType = SortTypes.POPULARITY))
                    }
                )
            }

            item {
                ToggleButtonChip(
                    enum = SortTypes.HEALTHINESS,
                    checked = uiState.value.filter.sort == SortTypes.HEALTHINESS,
                    onCheckedChange = { checked ->
                        onSortTypeChange(RecipeListPageEvent.SortTypeChange(sortType = SortTypes.HEALTHINESS))
                    }
                )
            }

            item {
                ToggleButtonChip(
                    enum = SortTypes.PROTEIN,
                    checked = uiState.value.filter.sort == SortTypes.PROTEIN,
                    onCheckedChange = { checked ->
                        onSortTypeChange(RecipeListPageEvent.SortTypeChange(sortType = SortTypes.PROTEIN))
                    }
                )
            }

            item {
                ToggleButtonChip(
                    enum = SortTypes.TOTAL_FAT,
                    checked = uiState.value.filter.sort == SortTypes.TOTAL_FAT,
                    onCheckedChange = { checked ->
                        onSortTypeChange(RecipeListPageEvent.SortTypeChange(sortType = SortTypes.TOTAL_FAT))
                    }
                )
            }

            item {
                ToggleButtonChip(
                    enum = SortTypes.CARBS,
                    checked = uiState.value.filter.sort == SortTypes.CARBS,
                    onCheckedChange = { checked ->
                        onSortTypeChange(RecipeListPageEvent.SortTypeChange(sortType = SortTypes.CARBS))
                    }
                )
            }
        }
    }
}