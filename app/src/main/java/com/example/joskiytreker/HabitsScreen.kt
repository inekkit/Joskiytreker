package com.example.joskiytreker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val title: String,
    val startDate: Long,
    val bestStreak: Int,
    val isGood: Boolean,
    val iconColorValue: Int,
    val iconEmoji: String = "⭐"
) {
    val iconColor: Color get() = Color(iconColorValue)
}

fun calculateStreak(startDate: Long): Int {
    val diff = System.currentTimeMillis() - startDate
    return (diff / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(0)
}

val habitsList = mutableStateListOf<Habit>()

@Composable
fun HabitsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToNewHabit: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "<", fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(end = 16.dp).clickable { onNavigateBack() })
                Text(text = "Привычки", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.surfaceVariant).clickable { onNavigateToNewHabit() }, contentAlignment = Alignment.Center) {
                Text(text = "+", fontSize = 28.sp, color = MaterialTheme.colorScheme.onSurface)
            }
        }

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.weight(1f).height(50.dp).clip(RoundedCornerShape(12.dp)).background(if (selectedTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant).clickable { selectedTab = 0 }, contentAlignment = Alignment.Center) {
                Text(text = "Полезные", color = if (selectedTab == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp)
            }
            Box(modifier = Modifier.weight(1f).height(50.dp).clip(RoundedCornerShape(12.dp)).background(if (selectedTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant).clickable { selectedTab = 1 }, contentAlignment = Alignment.Center) {
                Text(text = "Вредные", color = if (selectedTab == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        val filteredHabits = habitsList.filter { if (selectedTab == 0) it.isGood else !it.isGood }

        if (filteredHabits.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = if (selectedTab == 0) "Нет полезных привычек" else "Нет вредных привычек", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 16.dp)) {
                items(filteredHabits.size) { index ->
                    val habit = filteredHabits[index]
                    HabitListCard(
                        title = habit.title,
                        subtitle = "${calculateStreak(habit.startDate)} дней подряд",
                        iconBgColor = habit.iconColor,
                        iconEmoji = habit.iconEmoji
                    ) {
                        onNavigateToDetail(habitsList.indexOf(habit))
                    }
                }
            }
        }
    }
}

@Composable
fun HabitListCard(title: String, subtitle: String, iconBgColor: Color, iconEmoji: String, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(50)).background(iconBgColor), contentAlignment = Alignment.Center) {
                Text(text = iconEmoji, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                Text(text = subtitle, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(text = ">", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}