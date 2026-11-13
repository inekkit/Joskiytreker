package com.example.joskiytreker

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit = {}) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "<", fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(end = 16.dp).clickable { onNavigateBack() })
            Text(text = "Настройки", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }

        SettingsGroupTitle("Основные")
        SettingsCard {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🌙", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Тёмная тема", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                Switch(
                    checked = isDarkTheme.value,
                    onCheckedChange = {
                        isDarkTheme.value = it
                        saveAppData(context)
                    }
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            SettingsItem(icon = "🔔", title = "Уведомления") { Toast.makeText(context, "Уведомления", Toast.LENGTH_SHORT).show() }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            SettingsItem(icon = "⏰", title = "Напоминания") { Toast.makeText(context, "Напоминания", Toast.LENGTH_SHORT).show() }
        }

        SettingsGroupTitle("Приложение")
        SettingsCard {
            SettingsItem(icon = "🌐", title = "Язык", value = "Русский") { Toast.makeText(context, "Язык", Toast.LENGTH_SHORT).show() }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            SettingsItem(icon = "☁️", title = "Резервное копирование") { Toast.makeText(context, "Резервное копирование", Toast.LENGTH_SHORT).show() }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            SettingsItem(icon = "📄", title = "Экспорт данных") { Toast.makeText(context, "Экспорт", Toast.LENGTH_SHORT).show() }
        }

        SettingsGroupTitle("Безопасность")
        SettingsCard {
            SettingsItem(icon = "🔒", title = "PIN-код") { Toast.makeText(context, "PIN", Toast.LENGTH_SHORT).show() }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SettingsGroupTitle(title: String) {
    Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
fun SettingsCard(content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column { content() }
    }
}

@Composable
fun SettingsItem(icon: String, title: String, value: String? = null, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
        if (value != null) {
            Text(text = value, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(end = 8.dp))
        }
        Text(text = ">", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}