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

@Composable
fun AddFundsScreen(
    goalIndex: Int,
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current

    val goal = goalsList.getOrNull(goalIndex)

    if (goal == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Цель не найдена")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateBack) { Text("Назад") }
            }
        }
        return
    }

    var amountText by remember { mutableStateOf("3000") }

    val keyboardRows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(".", "0", "⌫")
    )

    fun onKeyPress(key: String) {
        if (key == "⌫") {
            if (amountText.isNotEmpty()) {
                amountText = amountText.dropLast(1)
            }
        } else if (key == ".") {
            if (!amountText.contains(".")) {
                amountText += key
            }
        } else {
            if (amountText == "0") {
                amountText = key
            } else {
                amountText += key
            }
        }
    }

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
                text = "Пополнить цель",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        // 2. Карточка с информацией о цели
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(50))
                        .background(goal.iconColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(goal.iconEmoji, fontSize = 24.sp) // <-- ИСПРАВЛЕНО: ЭМОДЗИ ИЗ ЦЕЛИ
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = goal.title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextDark)
                    Text(text = "${goal.currentAmount}/${goal.targetAmount}", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Поле ввода суммы
        Text(text = "Сумма", fontSize = 14.sp, color = TextDark, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            shape = RoundedCornerShape(12.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF66BB6A)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Кнопки Пополнить / Уменьшить
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    val amount = amountText.toIntOrNull() ?: 0
                    goal.currentAmount += amount
                    saveAppData(context) // <-- ДОБАВЛЕНО СОХРАНЕНИЕ
                    Toast.makeText(context, "Пополнено на $amount", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                },
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A))
            ) {
                Text(text = "Пополнить", fontSize = 14.sp, color = Color.White)
            }
            OutlinedButton(
                onClick = {
                    val amount = amountText.toIntOrNull() ?: 0
                    goal.currentAmount = (goal.currentAmount - amount).coerceAtLeast(0)
                    saveAppData(context) // <-- ДОБАВЛЕНО СОХРАНЕНИЕ
                    Toast.makeText(context, "Уменьшено на $amount", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                },
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF66BB6A))
                )
            ) {
                Text(text = "Уменьшить", fontSize = 14.sp, color = Color(0xFF66BB6A))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. Цифровая клавиатура
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            keyboardRows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { key ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (key == "⌫") Color(0xFFE0E0E0) else Color(0xFFF5F5F5))
                                .clickable { onKeyPress(key) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = key,
                                fontSize = 24.sp,
                                fontWeight = if (key == "⌫") FontWeight.Normal else FontWeight.Medium,
                                color = TextDark
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}