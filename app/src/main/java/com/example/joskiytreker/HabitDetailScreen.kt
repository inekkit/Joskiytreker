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
fun HabitDetailScreen(
    habitIndex: Int,
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val habit = habitsList.getOrNull(habitIndex)

    if (habit == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Привычка не найдена")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateBack) { Text("Назад") }
            }
        }
        return
    }

    val currentStreak = calculateStreak(habit.startDate)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        // Верхняя панель
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
                text = habit.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        // Блок с иконкой и названием
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(50))
                    .background(habit.iconColor),
                contentAlignment = Alignment.Center
            ) {
                Text(habit.iconEmoji, fontSize = 32.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = habit.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextDark
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Статистика
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardGreen)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$currentStreak",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(text = "Дней подряд", fontSize = 14.sp, color = TextDark.copy(alpha = 0.8f))
                }
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color.Gray.copy(alpha = 0.3f))
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${habit.bestStreak}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(text = "Лучшая серия", fontSize = 14.sp, color = TextDark.copy(alpha = 0.8f))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка "Сбросить серию"
        Button(
            onClick = {
                val newBestStreak = maxOf(habit.bestStreak, currentStreak)
                habitsList[habitIndex] = habit.copy(
                    startDate = System.currentTimeMillis(),
                    bestStreak = newBestStreak
                )
                saveAppData(context) // <-- ДОБАВЛЕНО СОХРАНЕНИЕ
                Toast.makeText(context, "Серия сброшена. Начинаем заново!", Toast.LENGTH_SHORT).show()
                onNavigateBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
        ) {
            Text(text = "Сбросить серию", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Удалить привычку"
        Button(
            onClick = {
                if (habitIndex in habitsList.indices) {
                    habitsList.removeAt(habitIndex)
                }
                saveAppData(context) // <-- ДОБАВЛЕНО СОХРАНЕНИЕ
                Toast.makeText(context, "Привычка удалена", Toast.LENGTH_SHORT).show()
                onNavigateBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
        ) {
            Text(text = "🗑️ Удалить привычку", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Пояснение
        Text(
            text = "Счётчик дней увеличивается автоматически. Нажмите «Сбросить серию», если сорвались.",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}