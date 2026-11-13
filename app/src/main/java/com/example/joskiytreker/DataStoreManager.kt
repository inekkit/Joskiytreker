package com.example.joskiytreker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class AppData(
    val habits: List<Habit> = emptyList(),
    val goals: List<Goal> = emptyList(),
    val profileName: String = "Имя",
    val profileStatus: String = "В пути к лучшей версии себя",
    val profileEmoji: String = "👤",
    val profileAvatarColorValue: Int = 0xFFE3F2FD.toInt(),
    val isDarkTheme: Boolean = false // <-- ДОБАВЛЕНО
)

object AppDataSerializer : Serializer<AppData> {
    override val defaultValue: AppData = AppData()

    override suspend fun readFrom(input: InputStream): AppData {
        return try {
            Json.decodeFromString(AppData.serializer(), input.readBytes().decodeToString())
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppData, output: OutputStream) {
        output.write(Json.encodeToString(AppData.serializer(), t).encodeToByteArray())
    }
}

val Context.dataStore: DataStore<AppData> by dataStore(
    fileName = "app_data.json",
    serializer = AppDataSerializer
)

fun saveAppData(context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        context.dataStore.updateData { currentData ->
            currentData.copy(
                habits = habitsList.toList(),
                goals = goalsList.toList(),
                profileName = profileName.value,
                profileStatus = profileStatus.value,
                profileEmoji = profileEmoji.value,
                profileAvatarColorValue = profileAvatarColorValue.value,
                isDarkTheme = isDarkTheme.value // <-- ДОБАВЛЕНО
            )
        }
    }
}