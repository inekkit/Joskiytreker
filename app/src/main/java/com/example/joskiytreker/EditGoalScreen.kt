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
                Text("Цель не найдена", color = MaterialTheme.colorScheme.onBackground)
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
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "<",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(end = 16.dp).clickable { onNavigateBack() }
            )
            Text(
                text = "Редактирование",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        @Composable
        fun EditTextField(label: String, value: String, onValueChange: (String) -> Unit, singleLine: Boolean = true, minHeight: Int = 0) {
            Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth().heightIn(min = minHeight.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = singleLine,
                trailingIcon = { Text(text = "✏️", fontSize = 16.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        EditTextField("Название", title, { title = it })
        EditTextField("Сумма цели", targetAmount, { if (it.all { c -> c.isDigit() }) targetAmount = it })
        EditTextField("Текущая сумма", currentAmount, { if (it.all { c -> c.isDigit() }) currentAmount = it })
        EditTextField("Описание", description, { description = it }, singleLine = false, minHeight = 120)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                goalsList[goalIndex] = goal.copy(
                    title = title,
                    targetAmount = targetAmount.toIntOrNull() ?: 0,
                    currentAmount = currentAmount.toIntOrNull() ?: 0,
                    description = description
                )
                saveAppData(context)
                Toast.makeText(context, "Изменения сохранены", Toast.LENGTH_SHORT).show()
                onNavigateBack()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Сохранить", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}