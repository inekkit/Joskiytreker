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
fun SettingsScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current

    // Оборачиваем в Column со скроллом, так как экран может не влезть целиком
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // 1. Верхняя панель
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "<",
                fontSize = 24.sp,
                color = TextDark,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { onNavigateBack() }
            )
            Text(
                text = "Настройки",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        // 2. Группа "Основные"
        SettingsGroupTitle("Основные")
        SettingsCard {
            SettingsItem(icon = "☀️", title = "Тема", value = "Светлая") {
                Toast.makeText(context, "Выбор темы", Toast.LENGTH_SHORT).show()
            }
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            SettingsItem(icon = "🔔", title = "Уведомления") {
                Toast.makeText(context, "Настройки уведомлений", Toast.LENGTH_SHORT).show()
            }
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            SettingsItem(icon = "⏰", title = "Напоминания") {
                Toast.makeText(context, "Настройки напоминаний", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Группа "Приложение"
        SettingsGroupTitle("Приложение")
        SettingsCard {
            SettingsItem(icon = "🌐", title = "Язык", value = "Русский") {
                Toast.makeText(context, "Выбор языка", Toast.LENGTH_SHORT).show()
            }
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            SettingsItem(icon = "☁️", title = "Резервное копирование") {
                Toast.makeText(context, "Резервное копирование", Toast.LENGTH_SHORT).show()
            }
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            SettingsItem(icon = "📄", title = "Экспорт данных") {
                Toast.makeText(context, "Экспорт данных", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Группа "Безопасность"
        SettingsGroupTitle("Безопасность")
        SettingsCard {
            SettingsItem(icon = "🔒", title = "PIN-код") {
                Toast.makeText(context, "Настройка PIN-кода", Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// Заголовок группы (например, "Основные")
@Composable
fun SettingsGroupTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

// Белая карточка, внутри которой лежат пункты
@Composable
fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            content()
        }
    }
}

// Один пункт меню (иконка, название, значение справа, стрелочка)
@Composable
fun SettingsItem(
    icon: String,
    title: String,
    value: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            color = TextDark,
            modifier = Modifier.weight(1f)
        )
        if (value != null) {
            Text(
                text = value,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Text(text = ">", fontSize = 20.sp, color = TextDark)
    }
}