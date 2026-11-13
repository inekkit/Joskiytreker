package com.example.joskiytreker

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditGoalScreen(
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

    var title by remember { mutableStateOf(goal.title) }
    var targetAmount by remember { mutableStateOf(goal.targetAmount.toString()) }
    var currentAmount by remember { mutableStateOf(goal.currentAmount.toString()) }
    var description by remember { mutableStateOf(goal.description) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // 1. Верхняя панель
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
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
                text = "Редактирование",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        // Функция для поля ввода
        @Composable
        fun EditTextField(
            label: String,
            value: String,
            onValueChange: (String) -> Unit,
            singleLine: Boolean = true,
            minHeight: Int = 0
        ) {
            Text(text = label, fontSize = 14.sp, color = TextDark, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minHeight.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = singleLine,
                trailingIcon = {
                    Text(text = "✏️", fontSize = 16.sp)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFF66BB6A)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 2. Поля ввода
        EditTextField(
            label = "Название",
            value = title,
            onValueChange = { title = it }
        )

        EditTextField(
            label = "Сумма цели",
            value = targetAmount,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) targetAmount = it
            }
        )

        EditTextField(
            label = "Текущая сумма",
            value = currentAmount,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) currentAmount = it
            }
        )

        EditTextField(
            label = "Описание",
            value = description,
            onValueChange = { description = it },
            singleLine = false,
            minHeight = 120
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Кнопка Сохранить (обновляет цель в списке)
        Button(
            onClick = {
                goalsList[goalIndex] = goal.copy(
                    title = title,
                    targetAmount = targetAmount.toIntOrNull() ?: 0,
                    currentAmount = currentAmount.toIntOrNull() ?: 0,
                    description = description
                )
                saveAppData(context) // <-- ДОБАВЛЕНО СОХРАНЕНИЕ
                Toast.makeText(context, "Изменения сохранены", Toast.LENGTH_SHORT).show()
                onNavigateBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A))
        ) {
            Text(text = "Сохранить", fontSize = 16.sp, color = Color.White)
        }
    }
}