package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.whattoeat.R
import com.example.whattoeat.presentation.ui.screens.custom_composable.FilterBottomSheet
import com.example.whattoeat.presentation.ui.screens.custom_composable.OffsetNavigationRow
import com.example.whattoeat.presentation.ui.screens.custom_composable.RecipeCard
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
                Text(stringResource(R.string.query_text_field_label))
            },
            value = uiState.value.filter.query ?: "",
            singleLine = true,
            onValueChange = {
                viewModel.reduce(
                    RecipeListPageEvent.QueryChange(it)
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

        if (uiState.value.isListShowing) {
            OffsetNavigationRow(viewModel = viewModel)
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(uiState.value.recipes) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    viewModel = viewModel
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    FilterBottomSheet(viewModel)
}