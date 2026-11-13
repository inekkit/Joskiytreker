package com.example.joskiytreker

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

val profileName = mutableStateOf("Имя")
val profileStatus = mutableStateOf("В пути к лучшей версии себя")
val profileEmoji = mutableStateOf("👤")
val profileAvatarColorValue = mutableStateOf(0xFFE3F2FD.toInt()) // <-- Int

val profileAvatarColor: Color
    get() = Color(profileAvatarColorValue.value)