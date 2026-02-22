package com.example.whattoeat.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import coil3.compose.AsyncImage
import com.example.whattoeat.R
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.presentation.ui.nav.Screen
import com.example.whattoeat.presentation.ui.screens.custom.FilterBottomSheet
import com.example.whattoeat.presentation.ui.theme.Black
import com.example.whattoeat.presentation.ui.viewModels.RecipeListModel
import com.example.whattoeat.presentation.ui.viewModels.RecipeListModelState
import com.example.whattoeat.presentation.ui.viewModels.RecipeListPageEvent
import com.example.whattoeat.presentation.ui.viewModels.RecipeListViewModel
import com.example.whattoeat.presentation.ui.viewModels.SearchType
import com.example.whattoeat.presentation.ui.viewModels.isDecreaseOffsetButtonEnabled
import com.example.whattoeat.presentation.ui.viewModels.isIncreaseOffsetButtonEnabled
import com.valentinilk.shimmer.shimmer

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeList(
    backStack: NavBackStack<Screen>,
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
            if (uiState.value.searchType == SearchType.COMPLEX_SEARCH) {
                IconButton(onClick = { viewModel.reduce(RecipeListPageEvent.IsFilterBottomSheetVisibleChange) }
                ) {
                    Icon(painter = painterResource(R.drawable.ic_filter), contentDescription = null)
                }
            }

            IconButton(
                onClick = { viewModel.reduce(RecipeListPageEvent.SearchTypeChange) }
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            }
        }

        var queryValue by remember {
            mutableStateOf(
                TextFieldValue(
                    text = uiState.value.filter.query.orEmpty(),
                    selection = TextRange(uiState.value.filter.query.orEmpty().length)
                )
            )
        }

        var productsValue by remember {
            mutableStateOf(
                TextFieldValue(
                    text = uiState.value.filter.products.orEmpty(),
                    selection = TextRange(uiState.value.filter.products.orEmpty().length)
                )
            )
        }

        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        val interactionSource = remember { MutableInteractionSource() }
        val isFocused by interactionSource.collectIsFocusedAsState()

        when (uiState.value.searchType) {
            SearchType.COMPLEX_SEARCH -> {
                OutlinedTextField(
                    value = queryValue,
                    onValueChange = {
                        queryValue = it

                        viewModel.reduce(
                            RecipeListPageEvent.QueryChange(it.text.trim())
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    label = {
                        Text(stringResource(R.string.query_text_field_label))
                    },
                    singleLine = true,
                    interactionSource = interactionSource,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        viewModel.reduce(RecipeListPageEvent.SearchButtonClicked)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                queryValue = queryValue.copy(text = "")
                                viewModel.reduce(event = RecipeListPageEvent.QueryChange(queryValue.text))
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                )
            }
            SearchType.SEARCH_BY_INGREDIENTS -> {
                OutlinedTextField(
                    value = productsValue,
                    onValueChange = {
                        productsValue = it

                        viewModel.reduce(
                            RecipeListPageEvent.ProductsChange(it.text.trim())
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    label = { Text(stringResource(R.string.products_text_field_label)) },
                    singleLine = true,
                    interactionSource = interactionSource,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        viewModel.reduce(RecipeListPageEvent.SearchButtonClicked)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                productsValue = productsValue.copy(text = "")
                                viewModel.reduce(event = RecipeListPageEvent.ProductsChange(productsValue.text))
                            }
                        ) { Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear") }
                    }
                )
            }
        }

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
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ) {
            Text(stringResource(R.string.search_button_text))
        }

        Spacer(Modifier.height(16.dp))

        if (!uiState.value.isListShowing && uiState.value.modelState is RecipeListModelState.LoadingState) {
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
        } else if (uiState.value.isListShowing && uiState.value.totalResults > 0 && uiState.value.searchType == SearchType.COMPLEX_SEARCH) {
            OffsetRecipeListNavigationRow(
                uiState = uiState,
                onIncreaseOffset = { viewModel.reduce(RecipeListPageEvent.IncreaseOffsetChange) },
                onDecreaseOffset = { viewModel.reduce(RecipeListPageEvent.DecreaseOffsetChange) },
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(uiState.value.recipesComplex) { recipe ->
                    RecipeComplexExtCard(
                        recipe = recipe,
                        onCardClick = { backStack.add(Screen.RecipeDetailDataObject(recipe.id)) },
                        onFavoriteRecipeChange = {
                            viewModel.reduce(
                                event = RecipeListPageEvent.FavoriteRecipeChange(
                                    recipe = recipe
                                )
                            )
                        },
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
        else if (uiState.value.isListShowing && uiState.value.totalResults > 0 && uiState.value.searchType == SearchType.SEARCH_BY_INGREDIENTS) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(uiState.value.recipesByIngredients) { recipe ->
                    RecipeByIngredientsExtCard(
                        recipe = recipe,
                        onCardClick = { backStack.add(Screen.RecipeDetailDataObject(recipe.id)) },
                        onFavoriteRecipeChange = {
                            viewModel.reduce(
                                event = RecipeListPageEvent.FavoriteRecipeChange(
                                    recipe = recipe
                                )
                            )
                        },
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }

    FilterBottomSheet(viewModel)
}

@Composable
private fun RecipeComplexExtCard(
    recipe: Recipe.RecipeComplexExt,
    onCardClick: () -> Unit,
    onFavoriteRecipeChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    Card(
        onClick = onCardClick,
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
                    IconButton(
                        onClick = onFavoriteRecipeChange
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
private fun RecipeByIngredientsExtCard(
    recipe: Recipe.RecipeByIngredientsExt,
    onCardClick: () -> Unit,
    onFavoriteRecipeChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    Card(
        onClick = onCardClick,
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
                    IconButton(
                        onClick = onFavoriteRecipeChange
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
private fun OffsetRecipeListNavigationRow(
    uiState: State<RecipeListModel>,
    onIncreaseOffset: () -> Unit,
    onDecreaseOffset: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(
            onClick = onDecreaseOffset,
            enabled = uiState.value.isDecreaseOffsetButtonEnabled()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = stringResource(R.string.previous_page)
            )
        }

        IconButton(
            onClick = onIncreaseOffset,
            enabled = uiState.value.isIncreaseOffsetButtonEnabled()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = stringResource(R.string.next_page)
            )
        }
    }
}