package com.example.joskiytreker

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Цвета
val BackgroundGray = Color(0xFFF7F7F7)
val TextDark = Color(0xFF2C2C2C)
val CardGreen = Color(0xFFE8F5E9)
val CardOrange = Color(0xFFFFF3E0)
val CardBlue = Color(0xFFE1F5FE)
val CardPurple = Color(0xFFF3E5F5)

@Composable
fun HomeScreen(
    onNavigateToHabitDetail: (Int) -> Unit = {},
    onNavigateToGoalDetail: (Int) -> Unit = {},
    onNavigateToGoals: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val context = LocalContext.current

    val topHabits = habitsList
        .sortedByDescending { calculateStreak(it.startDate) }
        .take(4)

    val topGoals = goalsList
        .sortedByDescending { goal ->
            if (goal.targetAmount > 0) goal.currentAmount.toFloat() / goal.targetAmount.toFloat() else 0f
        }
        .take(2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(horizontal = 16.dp)
    ) {
        // Верхняя панель
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Мой\nтрекер",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                lineHeight = 24.sp
            )
            Text(
                text = "⚙️",
                fontSize = 24.sp,
                modifier = Modifier.clickable { onNavigateToSettings() }
            )
        }

        // Блок привычек
        if (topHabits.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Нет привычек", color = Color.Gray, fontSize = 14.sp)
            }
        } else {
            topHabits.forEach { habit ->
                val streak = calculateStreak(habit.startDate)
                CompactCard(
                    title = habit.title,
                    value = "$streak дней",
                    subtitle = "Лучшая серия: ${habit.bestStreak} дней",
                    backgroundColor = habit.iconColor,
                    iconEmoji = habit.iconEmoji
                ) {
                    val realIndex = habitsList.indexOf(habit)
                    onNavigateToHabitDetail(realIndex)
                }
            }
        }

        // Заголовок "Мои цели"
        Text(
            text = "Мои цели",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
        )

        // Блок целей
        if (topGoals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Нет целей", color = Color.Gray, fontSize = 14.sp)
            }
        } else {
            topGoals.forEach { goal ->
                GoalCompactCard(goal = goal) {
                    val realIndex = goalsList.indexOf(goal)
                    onNavigateToGoalDetail(realIndex)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

// --- КАРТОЧКА ПРИВЫЧКИ ---
@Composable
fun CompactCard(
    title: String,
    value: String,
    subtitle: String,
    backgroundColor: Color,
    iconEmoji: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconEmoji, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextDark)
                Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text(text = subtitle, fontSize = 12.sp, color = TextDark.copy(alpha = 0.7f))
            }
            Text(text = ">", fontSize = 16.sp, color = TextDark)
        }
    }
}

// --- КАРТОЧКА ЦЕЛИ ---
@Composable
fun GoalCompactCard(
    goal: Goal,
    onClick: () -> Unit = {}
) {
    val progress = if (goal.targetAmount > 0) {
        goal.currentAmount.toFloat() / goal.targetAmount.toFloat()
    } else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50))
                    .background(goal.iconColor), // <-- ЦВЕТ ИЗ ЦЕЛИ
                contentAlignment = Alignment.Center
            ) {
                Text(text = goal.iconEmoji, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = goal.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextDark
                )
                Text(
                    text = "${goal.currentAmount}/${goal.targetAmount}",
                    fontSize = 12.sp,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = goal.progressColor,
                    trackColor = Color.LightGray.copy(alpha = 0.3f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = ">", fontSize = 16.sp, color = TextDark)
        }
    }
}