package com.example.joskiytreker

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
fun GoalDetailScreen(
    goalIndex: Int,
    onNavigateBack: () -> Unit = {},
    onNavigateToEdit: () -> Unit = {},
    onNavigateToAddFunds: () -> Unit = {}
) {
    val context = LocalContext.current

    val goal = goalsList.getOrNull(goalIndex)

    if (goal == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Цель не найдена", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateBack) { Text("Назад") }
            }
        }
        return
    }

    val currentAmount = goal.currentAmount.toString()
    val targetAmount = goal.targetAmount.toString()
    val description = goal.description

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        // 1. Верхняя панель
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "<",
                    fontSize = 24.sp,
                    color = TextDark,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { onNavigateBack() }
                )
                Text(
                    text = "Цели",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }
            Text(
                text = "✏️",
                fontSize = 20.sp,
                modifier = Modifier.clickable {
                    onNavigateToEdit()
                }
            )
        }

        // 2. Блок с иконкой и названием
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(50))
                        .background(goal.iconColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(goal.iconEmoji, fontSize = 28.sp) // <-- ИСПРАВЛЕНО: ЭМОДЗИ ИЗ ЦЕЛИ
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = goal.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextDark
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Текущая сумма
        Text(text = "Текущая сумма", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = currentAmount,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(text = "из $targetAmount", fontSize = 14.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 4. Прогресс-бар и проценты
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            val currentInt = goal.currentAmount
            val targetInt = goal.targetAmount
            val progress = if (targetInt > 0) (currentInt.toFloat() / targetInt.toFloat()).coerceIn(0f, 1f) else 0f

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = goal.progressColor,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(end = 4.dp, bottom = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Кнопка Пополнить/уменьшить
        Button(
            onClick = { onNavigateToAddFunds() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A))
        ) {
            Text(text = "Пополнить/уменьшить", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 6. Дата создания
        Text(text = "Дата создания", fontSize = 14.sp, color = Color.Gray)
        Text(text = "10 сентября 2026", fontSize = 16.sp, color = TextDark)

        Spacer(modifier = Modifier.height(16.dp))

        // 7. Описание
        Text(text = "Описание", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Text(
                text = if (description.isBlank()) "Нет описания" else description,
                modifier = Modifier.padding(12.dp),
                fontSize = 14.sp,
                color = TextDark
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 8. Кнопка Удалить цель
        Button(
            onClick = {
                if (goalIndex in goalsList.indices) {
                    goalsList.removeAt(goalIndex)
                }
                saveAppData(context) // <-- ДОБАВЛЕНО СОХРАНЕНИЕ
                Toast.makeText(context, "Цель удалена", Toast.LENGTH_SHORT).show()
                onNavigateBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
        ) {
            Text(text = "🗑️ Удалить цель", fontSize = 16.sp, color = Color.White)
        }
    }
}