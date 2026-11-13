package com.example.joskiytreker

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Тёмный текст для светлых пастельных карточек
private val DarkTextOnCard = Color(0xFF1C1C1C)

@Composable
fun HomeScreen(
    onNavigateToHabitDetail: (Int) -> Unit = {},
    onNavigateToGoalDetail: (Int) -> Unit = {},
    onNavigateToGoals: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val topHabits = habitsList.sortedByDescending { calculateStreak(it.startDate) }.take(4)
    val topGoals = goalsList.sortedByDescending { goal ->
        if (goal.targetAmount > 0) goal.currentAmount.toFloat() / goal.targetAmount.toFloat() else 0f
    }.take(2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        // Верхняя панель
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Мой\nтрекер",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 24.sp
            )
            Text(
                text = "⚙️",
                fontSize = 24.sp,
                modifier = Modifier.clickable { onNavigateToSettings() }
            )
        }

        // Привычки
        if (topHabits.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                Text(text = "Нет привычек", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            }
        } else {
            topHabits.forEach { habit ->
                CompactCard(
                    title = habit.title,
                    value = "${calculateStreak(habit.startDate)} дней",
                    subtitle = "Лучшая серия: ${habit.bestStreak} дней",
                    backgroundColor = habit.iconColor,
                    iconEmoji = habit.iconEmoji
                ) {
                    onNavigateToHabitDetail(habitsList.indexOf(habit))
                }
            }
        }

        // Заголовок "Мои цели"
        Text(
            text = "Мои цели",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
        )

        // Цели
        if (topGoals.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                Text(text = "Нет целей", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            }
        } else {
            topGoals.forEach { goal ->
                GoalCompactCard(goal = goal) {
                    onNavigateToGoalDetail(goalsList.indexOf(goal))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

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
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline) // <-- ГРАНИЦА
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconEmoji, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkTextOnCard)
                Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkTextOnCard)
                Text(text = subtitle, fontSize = 12.sp, color = DarkTextOnCard.copy(alpha = 0.7f))
            }
            Text(text = ">", fontSize = 16.sp, color = DarkTextOnCard)
        }
    }
}

@Composable
fun GoalCompactCard(goal: Goal, onClick: () -> Unit = {}) {
    val progress = if (goal.targetAmount > 0) goal.currentAmount.toFloat() / goal.targetAmount.toFloat() else 0f

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline) // <-- ГРАНИЦА
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(50)).background(goal.iconColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = goal.iconEmoji, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = goal.title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "${goal.currentAmount}/${goal.targetAmount}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = goal.progressColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = ">", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}