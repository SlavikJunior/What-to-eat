package com.example.whattoeat.presentation.ui.screens.custom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.whattoeat.R
import com.example.whattoeat.presentation.ui.viewModels.RecipeListPageEvent
import com.example.whattoeat.presentation.ui.viewModels.RecipeListViewModel

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
                    value = uiState.value.filter.includedProducts.orEmpty(),
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
                    value = uiState.value.filter.excludedProducts.orEmpty(),
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