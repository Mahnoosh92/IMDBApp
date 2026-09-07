package com.example.designsystem.theme.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbarHostState =
    staticCompositionLocalOf<SnackbarHostState> {
        error("No SnackbarHostState provided! Wrap your content in CompositionLocalProvider.")
    }
