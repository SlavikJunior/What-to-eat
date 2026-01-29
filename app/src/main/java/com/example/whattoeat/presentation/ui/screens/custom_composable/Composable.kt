package com.example.whattoeat.presentation.ui.screens.custom_composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerticalText(verticalText: String, textStyle: TextStyle = LocalTextStyle.current, modifier: Modifier = Modifier) {
    val verticalTextFormatted = verticalText.map { it }.joinToString("\n")

    Text(
        text = verticalTextFormatted,
        textAlign = TextAlign.Center,
        modifier = modifier,
        style = textStyle
    )
}

@Preview(showBackground = true)
@Composable
fun VerticalTextPreview() =
    VerticalText(
        verticalText = "SEARCH",
        modifier = Modifier.padding(4.dp),
        textStyle = TextStyle(
            fontSize = 22.sp,
            baselineShift = BaselineShift(0.314f)
        )
    )