package com.example.joskiytreker

import android.widget.Toast
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable

@Serializable
data class Goal(
    val title: String,
    var currentAmount: Int,
    val targetAmount: Int,
    val iconColorValue: Int, // <-- Int
    val progressColorValue: Int = 0xFF4CAF50.toInt(), // <-- Int
    val description: String = "",
    val iconEmoji: String = "🎯"
) {
    val iconColor: Color
        get() = Color(iconColorValue)
    val progressColor: Color
        get() = Color(progressColorValue)
}

val goalsList = mutableStateListOf<Goal>()

@Composable
fun GoalsScreen(
    onNavigateToDetail: (Int) -> Unit = {},
    onNavigateToNewGoal: () -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Цели", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(BackgroundGray)
                    .clickable { onNavigateToNewGoal() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "+", fontSize = 28.sp, color = TextDark)
            }
        }

        if (goalsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "У вас пока нет целей.\nНажмите '+', чтобы добавить.",
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(goalsList.size) { index ->
                    val goal = goalsList[index]
                    GoalItemCard(
                        title = goal.title,
                        currentAmount = goal.currentAmount,
                        targetAmount = goal.targetAmount,
                        iconColor = goal.iconColor,
                        progressColor = goal.progressColor,
                        iconEmoji = goal.iconEmoji
                    ) {
                        onNavigateToDetail(index)
                    }
                }
            }
        }
    }
}

@Composable
fun GoalItemCard(
    title: String,
    currentAmount: Int,
    targetAmount: Int,
    iconColor: Color,
    progressColor: Color,
    iconEmoji: String,
    onClick: () -> Unit = {}
) {
    val progress = if (targetAmount > 0) currentAmount.toFloat() / targetAmount.toFloat() else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(50))
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconEmoji, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = TextDark)
                Text(text = "$currentAmount/$targetAmount", fontSize = 14.sp, color = TextDark.copy(alpha = 0.7f))
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = progressColor,
                    trackColor = Color.LightGray.copy(alpha = 0.3f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = ">", fontSize = 20.sp, color = TextDark)
        }
    }
}