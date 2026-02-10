package com.example.whattoeat.presentation.ui.scaffoldElements

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.whattoeat.R

@Composable
fun WhatToEatFloatingActionButton(
    navController: NavHostController,
) {
    FloatingActionButton(
        onClick = { navController.navigate(route = TODO("Снавигировать на экран добавления рецепта")) },
        content = {
            Icon(
                painter = painterResource(R.drawable.ic_upload),
                contentDescription = "Upload recipe",
                modifier = Modifier.padding(16.dp)
            )
        },
        modifier = Modifier
            .padding(all = 8.dp)
            .size(64.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun WhatToEatFloatingActionButtonPreview() =
    WhatToEatFloatingActionButton(navController = rememberNavController())