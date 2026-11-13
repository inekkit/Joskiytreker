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
                Text("Цель не найдена", color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateBack) { Text("Назад") }
            }
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "<", fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(end = 16.dp).clickable { onNavigateBack() })
                Text(text = "Цели", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }
            Text(text = "✏️", fontSize = 20.sp, modifier = Modifier.clickable { onNavigateToEdit() })
        }

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(50)).background(goal.iconColor), contentAlignment = Alignment.Center) {
                    Text(goal.iconEmoji, fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = goal.title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Текущая сумма", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
            Text(text = goal.currentAmount.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Text(text = "из ${goal.targetAmount}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            val progress = if (goal.targetAmount > 0) (goal.currentAmount.toFloat() / goal.targetAmount.toFloat()).coerceIn(0f, 1f) else 0f
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
                color = goal.progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Text(text = "${(progress * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(end = 4.dp, bottom = 16.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { onNavigateToAddFunds() }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
            Text(text = "Пополнить/уменьшить", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Дата создания", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = "10 сентября 2026", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Описание", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
            Text(text = if (goal.description.isBlank()) "Нет описания" else goal.description, modifier = Modifier.padding(12.dp), fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (goalIndex in goalsList.indices) goalsList.removeAt(goalIndex)
                saveAppData(context)
                Toast.makeText(context, "Цель удалена", Toast.LENGTH_SHORT).show()
                onNavigateBack()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(text = "🗑️ Удалить цель", fontSize = 16.sp, color = MaterialTheme.colorScheme.onError)
        }
    }
}