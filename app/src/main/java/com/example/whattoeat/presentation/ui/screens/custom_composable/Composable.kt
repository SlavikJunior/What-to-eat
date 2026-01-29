package com.example.whattoeat.presentation.ui.screens.custom_composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.whattoeat.R
import com.example.whattoeat.domain.domain_entities.common.Recipe

@Composable
fun VerticalText(verticalText: String, modifier: Modifier = Modifier, textStyle: TextStyle = LocalTextStyle.current) {
    val verticalTextFormatted = verticalText.map { it }.joinToString("\n")

    Text(
        text = verticalTextFormatted,
        textAlign = TextAlign.Center,
        modifier = modifier,
        style = textStyle
    )
}


@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp)
) {
    Card(
        onClick = { onClick() },
        modifier = modifier,
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = recipe.image,
                contentDescription = stringResource(R.string.recipe_card_image_content_description, recipe.title),
                modifier = Modifier.size(100.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = recipe.title,
                    modifier = Modifier.weight(2f),
                    style = TextStyle(
                        fontSize = 22.sp,
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = recipe.description,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecipeCardPreview() =
    RecipeCard(
        recipe = Recipe("Блины", "Ещё один рецепт вкусных блинов"),
        onClick = {},
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    )