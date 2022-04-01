package com.kvr.user.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppUserTheme( content: @Composable() () -> Unit) {
    MaterialTheme(
            colors = lightColors(
                primary = PrimaryColor,
                primaryVariant = PrimaryColor,
                secondary = PrimaryColor,
                background = BgColor,
            ),
            content = content,
    )
}