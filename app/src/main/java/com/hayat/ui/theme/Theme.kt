package com.hayat.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection

private val DarkColorScheme = darkColorScheme(
    primary = White,
    secondary = Gray,
    tertiary = LightGray,
    background = Black,
    surface = DarkGray,
    onPrimary = Black,
    onSecondary = Black,
    onTertiary = Black,
    onBackground = White,
    onSurface = White,
    error = Red
)

private val LightColorScheme = lightColorScheme(
    primary = Black,
    secondary = DarkGray,
    tertiary = Gray,
    background = White,
    surface = LightGray,
    onPrimary = White,
    onSecondary = White,
    onTertiary = Black,
    onBackground = Black,
    onSurface = Black,
    error = Red
)

// Helper function to safely find Activity from Context
private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun HayatTheme(
    darkTheme: Boolean? = null, // null means use system default
    rtl: Boolean = true, // Default to RTL for Arabic support
    content: @Composable () -> Unit
) {
    val useDarkTheme = darkTheme ?: isSystemInDarkTheme()
    val colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            view.context.findActivity()?.window?.let { window ->
                window.statusBarColor = colorScheme.background.toArgb()
            }
        }
    }

    val layoutDirection = if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}