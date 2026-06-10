package com.applogin.loginyregistro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AuthPrimary,
    secondary = AuthSecondary,
    tertiary = AuthTertiary,
    background = AuthBackground,
    surface = AuthSurface
)

private val DarkColorScheme = darkColorScheme(
    primary = AuthPrimary,
    secondary = AuthSecondary,
    tertiary = AuthTertiary,
    background = AuthDarkBackground,
    surface = AuthDarkSurface
)

@Composable
fun LoginyRegistroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
