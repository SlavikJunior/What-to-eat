package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.whattoeat.R
import com.example.whattoeat.presentation.ui.nav.RecipeDetailDataObject
import com.example.whattoeat.presentation.ui.screens.custom_composable.FilterBottomSheet
import com.example.whattoeat.presentation.ui.screens.custom_composable.RecipeCard
import com.example.whattoeat.presentation.ui.view_models.RecipeDetailViewModel
import com.example.whattoeat.presentation.ui.view_models.RecipeListPageEvent
import com.example.whattoeat.presentation.ui.view_models.RecipeListViewModel

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
                Text(stringResource(R.string.recipe_title_text_field_label))
            },
            value = uiState.value.filter.recipeTitle,
            singleLine = true,
            onValueChange = {
                viewModel.reduce(
                    RecipeListPageEvent.RecipeTitleChange(it)
                )
            }
        )

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
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

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(uiState.value.recipes) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    onClick = {
                        navController.navigate(
                            RecipeDetailDataObject(recipe)
                        )
                    }
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    FilterBottomSheet(viewModel)
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun RecipeListPreview() {
    RecipeList(navController = rememberNavController())
}