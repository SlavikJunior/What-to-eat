package com.example.whattoeat.presentation.ui.scaffoldElements

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import kotlinx.coroutines.CoroutineScope

data class SnackbarHelper(
    val snackHostState: SnackbarHostState,
    val coroutineScope: CoroutineScope
)

enum class SnackbarType {
    INFO,
    SUCCESS,
    WARNING,
    ERROR
}

data class SnackbarVisualsCustom(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = true,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    val contentAlignment: Alignment = Alignment.BottomCenter,
    val type: SnackbarType = SnackbarType.ERROR,
    val onClickAction: () -> Unit = {}
) : SnackbarVisuals

object WhatToEatSnackbar {

    @Composable
    fun SuccessSnackbar(
        message: String = "Well Done",
        duration: SnackbarDuration = SnackbarDuration.Short,
        contentAlignment: Alignment = Alignment.BottomCenter,
        type: SnackbarType = SnackbarType.SUCCESS,
    ) {
        SnackbarVisualsCustom(
            message = message,
            duration = duration,
            contentAlignment = contentAlignment,
            type = type,
        )
    }

    @Composable
    fun WarningSnackbar(
        message: String = "Well Done",
        duration: SnackbarDuration = SnackbarDuration.Long,
        contentAlignment: Alignment = Alignment.BottomCenter,
        type: SnackbarType = SnackbarType.WARNING,
    ) {
        SnackbarVisualsCustom(
            message = message,
            duration = duration,
            contentAlignment = contentAlignment,
            type = type,
        )
    }

    @Composable
    fun ErrorSnackbar(
        message: String = "Well Done",
        actionLabel: String = "",
        withDismissAction: Boolean = true,
        duration: SnackbarDuration = SnackbarDuration.Indefinite,
        contentAlignment: Alignment = Alignment.BottomCenter,
        type: SnackbarType = SnackbarType.ERROR,
        onClickAction: () -> Unit = {}
    ) {
        SnackbarVisualsCustom(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration,
            contentAlignment = contentAlignment,
            type = type,
            onClickAction = onClickAction,
        )
    }

    @Composable
    fun InfoSnackbar(
        message: String = "Well Done",
        duration: SnackbarDuration = SnackbarDuration.Short,
        contentAlignment: Alignment = Alignment.BottomCenter,
        type: SnackbarType = SnackbarType.INFO,
    ) {
        SnackbarVisualsCustom(
            message = message,
            duration = duration,
            contentAlignment = contentAlignment,
            type = type,
        )
    }
}