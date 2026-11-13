package com.example.joskiytreker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.joskiytreker.ui.theme.JoskiytrekerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoskiytrekerTheme(darkTheme = isDarkTheme.value) {
                val context = LocalContext.current

                // --- ЗАГРУЗКА ДАННЫХ ПРИ СТАРТЕ ---
                LaunchedEffect(Unit) {
                    context.dataStore.data.collect { data ->
                        habitsList.clear()
                        habitsList.addAll(data.habits)
                        goalsList.clear()
                        goalsList.addAll(data.goals)
                        profileName.value = data.profileName
                        profileStatus.value = data.profileStatus
                        profileEmoji.value = data.profileEmoji
                        profileAvatarColorValue.value = data.profileAvatarColorValue
                        isDarkTheme.value = data.isDarkTheme
                    }
                }

                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // --- ГЛАВНАЯ ---
                        composable("home") {
                            HomeScreen(
                                onNavigateToHabitDetail = { index ->
                                    navController.navigate("habit_detail/$index")
                                },
                                onNavigateToGoalDetail = { index ->
                                    navController.navigate("goal_detail/$index")
                                },
                                onNavigateToGoals = { navController.navigate("goals") },
                                onNavigateToSettings = { navController.navigate("settings") }
                            )
                        }

                        // --- ПРИВЫЧКИ ---
                        composable("habits") {
                            HabitsScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToNewHabit = { navController.navigate("new_habit") },
                                onNavigateToDetail = { index ->
                                    navController.navigate("habit_detail/$index")
                                }
                            )
                        }

                        composable("new_habit") {
                            NewHabitScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("habit_detail/{index}") { backStackEntry ->
                            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
                            HabitDetailScreen(
                                habitIndex = index,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // --- ЦЕЛИ ---
                        composable("goals") {
                            GoalsScreen(
                                onNavigateToDetail = { index ->
                                    navController.navigate("goal_detail/$index")
                                },
                                onNavigateToNewGoal = { navController.navigate("new_goal") }
                            )
                        }

                        composable("new_goal") {
                            NewGoalScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("goal_detail/{index}") { backStackEntry ->
                            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
                            GoalDetailScreen(
                                goalIndex = index,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToEdit = { navController.navigate("edit_goal/$index") },
                                onNavigateToAddFunds = { navController.navigate("add_funds/$index") }
                            )
                        }

                        composable("add_funds/{index}") { backStackEntry ->
                            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
                            AddFundsScreen(
                                goalIndex = index,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("edit_goal/{index}") { backStackEntry ->
                            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
                            EditGoalScreen(
                                goalIndex = index,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // --- ПРОФИЛЬ И НАСТРОЙКИ ---
                        composable("profile") {
                            ProfileScreen(
                                onNavigateBack = { navController.popBackStack() }, // <-- ДОБАВЛЕНО
                                onNavigateToSettings = { navController.navigate("settings") },
                                onNavigateToEditProfile = { navController.navigate("edit_profile") },
                                onNavigateToGoals = { navController.navigate("goals") }
                            )
                        }

                        composable("edit_profile") {
                            EditProfileScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("settings") {
                            SettingsScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Нижняя панель навигации
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationBarItem(
            icon = { Text("🏠", fontSize = 20.sp) },
            label = { Text("Главная", fontSize = 10.sp) },
            selected = currentRoute == "home",
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = { Text("⭐", fontSize = 20.sp) },
            label = { Text("Привычки", fontSize = 10.sp) },
            selected = currentRoute == "habits",
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = {
                navController.navigate("habits") {
                    popUpTo("home")
                }
            }
        )
        NavigationBarItem(
            icon = { Text("🎯", fontSize = 20.sp) },
            label = { Text("Цели", fontSize = 10.sp) },
            selected = currentRoute == "goals",
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = {
                navController.navigate("goals") {
                    popUpTo("home")
                }
            }
        )
        NavigationBarItem(
            icon = { Text("👤", fontSize = 20.sp) },
            label = { Text("Профиль", fontSize = 10.sp) }, // <-- ИСПРАВЛЕНО (убрал лишний composable)
            selected = currentRoute == "profile",
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSurface,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = {
                navController.navigate("profile") {
                    popUpTo("home")
                }
            }
        )
    }
}