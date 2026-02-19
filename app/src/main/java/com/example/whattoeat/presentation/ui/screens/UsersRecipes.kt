package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.whattoeat.R
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.presentation.ui.nav.UsersRecipeDetailDataObject
import com.example.whattoeat.presentation.ui.viewModels.ButtonActionType
import com.example.whattoeat.presentation.ui.viewModels.SearchType
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipeDetailModel
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipesModelState
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipesPageEvent
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersRecipes(
    navController: NavHostController,
    openAddSheet: Boolean,
    viewModel: UsersRecipesViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(),
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(openAddSheet) {
        try {
            if (openAddSheet) {
                viewModel.reduce(UsersRecipesPageEvent.IsAddSheetVisibleChange)

                navController.currentBackStackEntry?.arguments?.putBoolean("openAddSheet", false)
            }
        } catch (e: Exception) {
            // Обработка ошибки, если аргументы не переданы или не тот роут
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
                        contentDescription = "Upload recipe",
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
                        text = "Error: ${(uiState.value.modelState as UsersRecipesModelState.ErrorState).cause?.message}",
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
                            text = "У вас пока нет рецептов.",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Нажмите +, чтобы создать.",
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
                                     navController.navigate(UsersRecipeDetailDataObject(recipeId = recipe.id))
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
                    recipe.readyInMinutes?.let {
                        if (it > 0) {
                            Icon(
                                painter = painterResource(R.drawable.ic_timer),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${recipe.readyInMinutes} мин",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }

                    recipe.servings?.let {
                        if (it > 0) {
                            Icon(
                                painter = painterResource(R.drawable.ic_restaurant),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${recipe.servings} порц.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                recipe.notes?.let {
                    if (it.isNotBlank()) {
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
                        contentDescription = "Update",
                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
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
        val labelText = if (uiState.value.buttonActionType == ButtonActionType.SAVE_RECIPE) "New Recipe"
                else "Update recipe"

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
            label = { Text("Title") },
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
            label = { Text(text = "Cooking Time (minutes)") },
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
            label = { Text(text = "Servings (count)") },
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
            label = { Text("Ingredients") }
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
            label = { Text("Notes") },
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
