package com.example.whattoeat.presentation.ui.scaffoldElements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavBackStack
import com.example.whattoeat.R
import com.example.whattoeat.presentation.ui.nav.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatToEatTopAppBar(
    backStack: NavBackStack<Screen>
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (backStack.size > 1) {
                IconButton(onClick = { backStack.removeLastOrNull() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_content_description)
                    )
                }
            }
        }
    )
}