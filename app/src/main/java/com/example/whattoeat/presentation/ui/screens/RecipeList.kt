package com.example.whattoeat.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.whattoeat.R
import com.example.whattoeat.presentation.ui.screens.custom_composable.VerticalText
import com.example.whattoeat.presentation.view_models.RecipeListViewModel

@Composable
fun RecipeList(
    navController: NavHostController,
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: RecipeListViewModel = viewModel(factory = RecipeListViewModel.Factory)
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier
                .weight(1F)
                .padding(paddingValues = paddingValues)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    modifier = Modifier.fillMaxHeight(),
                    enabled = true,
                    onClick = {}
                ) {
                    VerticalText(
                        verticalText = "SEARCH",
                        modifier = Modifier.padding(horizontal = 4.dp),
                        textStyle = TextStyle(
                            fontSize = 22.sp,
                            baselineShift = BaselineShift(0.314f)
                        )
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var inputTextFieldTitle by rememberSaveable { mutableStateOf("") }
                    var inputTextFieldInclusiveProducts by rememberSaveable { mutableStateOf("") }
                    var inputTextFieldExclusiveProducts by rememberSaveable { mutableStateOf("") }

                    OutlinedTextField(
                        label = {
                            Text(
                                text = stringResource(R.string.recipe_title_text_field_label)
                            )
                        },
                        value = inputTextFieldTitle,
                        singleLine = true,
                        onValueChange = { newValue ->
                            inputTextFieldTitle = newValue
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        label = {
                            Text(
                                text = stringResource(R.string.inclusive_products_text_field_label)
                            )
                        },
                        value = inputTextFieldInclusiveProducts,
                        onValueChange = { newValue ->
                            inputTextFieldInclusiveProducts = newValue
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        label = {
                            Text(
                                text = stringResource(R.string.exclusive_products_text_field_label)
                            )
                        },
                        value = inputTextFieldExclusiveProducts,
                        onValueChange = { newValue ->
                            inputTextFieldExclusiveProducts = newValue
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Surface(modifier = Modifier.weight(1F)) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                for(i in 0 until 5) {
                    Card(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Recipe $i"
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecipeListPreview() =
    RecipeList(
        navController = rememberNavController()
    )