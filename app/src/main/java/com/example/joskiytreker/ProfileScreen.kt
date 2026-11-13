package com.example.joskiytreker

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToGoals: () -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        // 1. Верхняя панель
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "<",
                    fontSize = 24.sp,
                    color = TextDark,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = "Профиль",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }
            // Иконка карандаша
            Text(
                text = "✏️",
                fontSize = 24.sp,
                modifier = Modifier.clickable {
                    onNavigateToEditProfile()
                }
            )
        }

        // 2. Блок с аватаром и именем
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Аватар с эмодзи и цветом
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(50))
                    .background(profileAvatarColor), // <-- ИСПРАВЛЕНО: без .value
                contentAlignment = Alignment.Center
            ) {
                Text(profileEmoji.value, fontSize = 40.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = profileName.value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = profileStatus.value,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Блок статистики
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(number = "${habitsList.size}", label = "привычек", modifier = Modifier.weight(1f))
            StatCard(number = "${goalsList.size}", label = "цели", modifier = Modifier.weight(1f))
            StatCard(number = "0", label = "дней без\nсрывов", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Список пунктов меню
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileMenuItem(icon = "🎯", title = "Мои цели") {
                onNavigateToGoals()
            }
            ProfileMenuItem(icon = "📊", title = "Статистика") {
                Toast.makeText(context, "Статистика", Toast.LENGTH_SHORT).show()
            }
            ProfileMenuItem(icon = "⭐", title = "Достижения") {
                Toast.makeText(context, "Достижения", Toast.LENGTH_SHORT).show()
            }
            ProfileMenuItem(icon = "⚙️", title = "Настройки") {
                onNavigateToSettings()
            }
            ProfileMenuItem(icon = "ℹ️", title = "О приложении") {
                Toast.makeText(context, "О приложении", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

// Компонент карточки статистики
@Composable
fun StatCard(number: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

// Компонент пункта меню
@Composable
fun ProfileMenuItem(icon: String, title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextDark,
                modifier = Modifier.weight(1f)
            )
            Text(text = ">", fontSize = 20.sp, color = TextDark)
        }
    }
}