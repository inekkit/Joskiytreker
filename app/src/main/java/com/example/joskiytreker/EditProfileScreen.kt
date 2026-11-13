package com.example.joskiytreker

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditProfileScreen(onNavigateBack: () -> Unit = {}) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(profileName.value) }
    var status by remember { mutableStateOf(profileStatus.value) }
    var selectedEmoji by remember { mutableStateOf(profileEmoji.value) }
    var selectedColor by remember { mutableStateOf(profileAvatarColor) }

    val availableEmojis = listOf("👤", "😀", "😎", "🦸", "🐱", "🐶", "🦊", "🐼", "🚀", "🌟", "🔥", "🍀")
    val availableColors = listOf(
        Color(0xFFB2DFDB), Color(0xFFFFCCBC), Color(0xFFC5CAE9),
        Color(0xFFF8BBD0), Color(0xFFDCEDC8), Color(0xFFFFF9C4)
    )

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 24.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "<", fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(end = 16.dp).clickable { onNavigateBack() })
                Text(text = "Редактирование профиля", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }

            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(50)).background(selectedColor), contentAlignment = Alignment.Center) {
                    Text(selectedEmoji, fontSize = 50.sp)
                }
            }

            Text(text = "Имя", fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.outline, focusedBorderColor = MaterialTheme.colorScheme.primary))

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Статус", fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(value = status, onValueChange = { status = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.outline, focusedBorderColor = MaterialTheme.colorScheme.primary))

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Эмодзи аватара", fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(6), modifier = Modifier.fillMaxWidth().height(120.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(availableEmojis) { emoji ->
                    Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(if (selectedEmoji == emoji) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant).clickable { selectedEmoji = emoji }, contentAlignment = Alignment.Center) {
                        Text(text = emoji, fontSize = 24.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Цвет фона аватара", fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                availableColors.forEach { color ->
                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(color).border(width = if (selectedColor == color) 3.dp else 1.dp, color = if (selectedColor == color) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, shape = CircleShape).clickable { selectedColor = color })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                profileName.value = name
                profileStatus.value = status
                profileEmoji.value = selectedEmoji
                profileAvatarColorValue.value = selectedColor.toArgb()
                saveAppData(context)
                Toast.makeText(context, "Профиль сохранён", Toast.LENGTH_SHORT).show()
                onNavigateBack()
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp).height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Сохранить", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}