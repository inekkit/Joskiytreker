package com.example.joskiytreker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- ТЁМНАЯ ТЕМА ---
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF66BB6A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4CAF50),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF90CAF9),
    onSecondary = Color.Black,
    background = Color(0xFF2C2C2C), // <-- Тёмно-серый фон
    onBackground = Color(0xFFF0F0F0), // <-- Светлый текст на фоне
    surface = Color(0xFF3A3A3A), // <-- Цвет карточек (чуть светлее фона)
    onSurface = Color(0xFFF0F0F0), // <-- Текст на карточках
    surfaceVariant = Color(0xFF4A4A4A),
    onSurfaceVariant = Color(0xFFB0B0B0),
    outline = Color(0xFF666666), // <-- Цвет границ
    error = Color(0xFFEF5350),
    onError = Color.White
)

// --- СВЕТЛАЯ ТЕМА ---
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF66BB6A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA5D6A7),
    onPrimaryContainer = Color(0xFF1B5E20),
    secondary = Color(0xFF90CAF9),
    onSecondary = Color.Black,
    background = Color(0xFFF7F7F7),
    onBackground = Color(0xFF1C1C1C),
    surface = Color.White,
    onSurface = Color(0xFF1C1C1C),
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = Color(0xFF666666),
    outline = Color(0xFFE0E0E0),
    error = Color(0xFFE57373),
    onError = Color.White
)

@Composable
fun JoskiytrekerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // <-- ОТКЛЮЧАЕМ ДИНАМИЧЕСКИЕ ЦВЕТА (иначе наши не применятся)
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}