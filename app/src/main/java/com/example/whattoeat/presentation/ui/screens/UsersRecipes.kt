package com.example.whattoeat.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.whattoeat.domain.domainEntities.common.Recipe
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipesPageEvent
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipesModelState
import com.example.whattoeat.presentation.ui.viewModels.UsersRecipesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersRecipes(
    navController: NavHostController,
    viewModel: UsersRecipesViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues()
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.padding(paddingValues)
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            Button(
                onClick = { viewModel.reduce(event = UsersRecipesPageEvent.IsAddSheetVisibleChange) }
            ) {
                Text(
                    text = if (uiState.value.isAddSheetVisible) "Показывается"
                    else "Не показывается"
                )
            }

            if (uiState.value.isAddSheetVisible) {
                Text("Add sheet visible!!!")

                Log.d("TEST TAG", "Add sheet visible!")
            }

            if (uiState.value.modelState is UsersRecipesModelState.LoadingState && uiState.value.recipes.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.value.recipes.isEmpty()) {
                Text(
                    text = "No recipes yet. Add one!",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.value.modelState is UsersRecipesModelState.ErrorState) {
                Text(
                    text = "Error: ${(uiState.value.modelState as UsersRecipesModelState.ErrorState).cause}!",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.value.recipes) { recipe ->
                        UserRecipeItem(
                            recipe = recipe,
                            onDeleteClick = { viewModel.reduce(UsersRecipesPageEvent.DeleteRecipe(recipe)) }
                        )
                    }
                }
            }
        }

        if (uiState.value.isAddSheetVisible) {
            val sheetState = rememberModalBottomSheetState()
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
fun UserRecipeItem(
    recipe: Recipe.RecipeByUser,
    onDeleteClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = recipe.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${recipe.readyInMinutes} mins",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddRecipeForm(viewModel: UsersRecipesViewModel) {
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("New Recipe", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = uiState.value.recipe.title,
            onValueChange = { newTitle -> viewModel.reduce(event = UsersRecipesPageEvent.OnTitleChange(title = newTitle)) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.value.recipe.readyInMinutes.toString(),
            onValueChange = { newTime -> viewModel.reduce(event = UsersRecipesPageEvent.OnTimeChange(readyInMinutes = newTime.toInt())) },
            label = { Text(text = "Cooking Time (minutes)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.reduce(event = UsersRecipesPageEvent.SaveRecipe) },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.value.recipe.title.isNotBlank()
        ) {
            Text("Save Recipe")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}