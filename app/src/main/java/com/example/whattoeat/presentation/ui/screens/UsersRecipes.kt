package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import com.example.whattoeat.R
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.presentation.ui.nav.Screen
import com.example.whattoeat.presentation.ui.viewModels.ButtonActionType
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipesModelState
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipesPageEvent
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersRecipes(
    backStack: NavBackStack<Screen>,
    openAddSheet: Boolean,
    viewModel: UsersRecipesViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(),
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (openAddSheet) {
            viewModel.reduce(UsersRecipesPageEvent.IsAddSheetVisibleChange)
            backStack[backStack.size - 1] = Screen.UsersRecipesScreen(openAddSheet = false)
        }
    }

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.reduce(UsersRecipesPageEvent.IsAddSheetVisibleChange) },
                content = {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = stringResource(R.string.upload_users_recipe_content_description),
                        modifier = Modifier.padding(16.dp)
                    )
                },
                modifier = Modifier
                    .padding(all = 8.dp)
                    .size(64.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.value.modelState is UsersRecipesModelState.LoadingState && uiState.value.recipes.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.value.modelState is UsersRecipesModelState.ErrorState -> {
                    Text(
                        text = stringResource(
                            id = R.string.error_state_label_format,
                            ((uiState.value.modelState as UsersRecipesModelState.ErrorState).cause?.message).orEmpty()
                        ),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                uiState.value.recipes.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.empty_loading_users_label),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.empty_loading_users_hint),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.value.recipes) { recipe ->
                            UserRecipeCard(
                                recipe = recipe,
                                onCardClick = {
                                    backStack.add(Screen.UsersRecipeDetailScreen(recipe.id))
                                },
                                onDeleteClick = {
                                    viewModel.reduce(UsersRecipesPageEvent.DeleteRecipe(recipe))
                                },
                                onUpdateCLick = {
                                    viewModel.reduce(UsersRecipesPageEvent.UpdateRecipeStart(recipe))
                                }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(64.dp)) }
                    }
                }
            }
        }

        if (uiState.value.isSheetVisible) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { viewModel.reduce(UsersRecipesPageEvent.IsAddSheetVisibleChange) },
                sheetState = sheetState
            ) {
                AddRecipeForm(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun UserRecipeCard(
    recipe: Recipe.RecipeByUser,
    onCardClick: () -> Unit,
    onUpdateCLick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        onClick = onCardClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    if (recipe.readyInMinutes != null && recipe.readyInMinutes > 0) {
                        Icon(
                            painter = painterResource(R.drawable.ic_timer),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(
                                R.string.cooking_time_label_format,
                                recipe.readyInMinutes
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    if (recipe.servings != null && recipe.servings > 0) {
                        Icon(
                            painter = painterResource(R.drawable.ic_restaurant),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.servings_label_format, recipe.servings),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                if (recipe.notes != null && recipe.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recipe.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = onUpdateCLick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.update_users_recipe_content_description),
                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_users_recipe_content_description),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddRecipeForm(viewModel: UsersRecipesViewModel) {
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val labelText =
            if (uiState.value.buttonActionType == ButtonActionType.SAVE_RECIPE)
                stringResource(R.string.upload_users_recipe_label)
            else
                stringResource(R.string.update_users_recipe_label)

        Text(labelText, style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = uiState.value.recipeByUserOnUi.title,
            onValueChange = { newTitle ->
                viewModel.reduce(
                    event = UsersRecipesPageEvent.TitleChange(
                        title = newTitle
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.title_field)) },
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.value.recipeByUserOnUi.readyInMinutes,
            onValueChange = { newTime ->
                viewModel.reduce(
                    event = UsersRecipesPageEvent.ReadyInMinutesChange(
                        readyInMinutes = newTime
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.cooking_time_field)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = uiState.value.recipeByUserOnUi.servings,
            onValueChange = { newServings ->
                viewModel.reduce(
                    event = UsersRecipesPageEvent.ServingsChange(
                        servings = newServings
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.servings_field)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = uiState.value.recipeByUserOnUi.ingredients,
            onValueChange = { newIngredients ->
                viewModel.reduce(
                    event = UsersRecipesPageEvent.IngredientsChange(
                        ingredients = newIngredients
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.ingredients_field)) }
        )

        OutlinedTextField(
            value = uiState.value.recipeByUserOnUi.notes,
            onValueChange = { newNotes ->
                viewModel.reduce(
                    event = UsersRecipesPageEvent.NotesChange(
                        notes = newNotes
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.notes_field)) },
        )

        Button(
            onClick = {
                if (uiState.value.buttonActionType == ButtonActionType.SAVE_RECIPE)
                    viewModel.reduce(event = UsersRecipesPageEvent.SaveRecipe)
                else
                    viewModel.reduce(event = UsersRecipesPageEvent.UpdateRecipeEnd)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.value.recipeByUserOnUi.title.isNotBlank()
        ) {
            Text(uiState.value.buttonActionType.text)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}