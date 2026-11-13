package com.example.joskiytreker

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

// Глобальные данные профиля
val profileName = mutableStateOf("Имя")
val profileStatus = mutableStateOf("В пути к лучшей версии себя")
val profileEmoji = mutableStateOf("👤")
val profileAvatarColorValue = mutableStateOf(0xFFE3F2FD.toInt())

// Вычисляемое свойство для отображения
val profileAvatarColor: Color
    get() = Color(profileAvatarColorValue.value)

// --- НОВОЕ: Тёмная тема ---
val isDarkTheme = mutableStateOf(false) // false = светлая, true = тёмная