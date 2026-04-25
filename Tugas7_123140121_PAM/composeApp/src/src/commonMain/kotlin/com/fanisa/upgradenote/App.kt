package com.fanisa.upgradenote

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fanisa.upgradenote.presentation.ui.AddEditNoteScreen
import com.fanisa.upgradenote.presentation.ui.NotesScreen
import com.fanisa.upgradenote.presentation.ui.ProfileScreen
import com.fanisa.upgradenote.presentation.ui.SettingsScreen
import com.fanisa.upgradenote.presentation.viewmodel.NotesViewModel

@Composable
fun App(viewModel: NotesViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentTheme by viewModel.theme.collectAsState()

    val maroon = Color(0xFF800000)
    val lightPink = Color(0xFFFFE4E1)
    val girlyPink = Color(0xFFFFC0CB)

    val colorScheme = when (currentTheme) {
        "pink" -> lightColorScheme(
            primary = girlyPink,
            onPrimary = Color.White,
            secondary = Color.Black,
            surface = Color(0xFFFFF0F5)
        )
        "black" -> darkColorScheme(
            primary = Color.White,
            onPrimary = Color.Black,
            secondary = Color.Gray,
            surface = Color.Black
        )
        else -> lightColorScheme(
            primary = maroon,
            onPrimary = Color.White,
            secondary = Color.Black,
            surface = Color.White
        )
    }

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            bottomBar = {
                val showBottomBar = currentDestination?.route in listOf("notes_list", "profile")
                if (showBottomBar) {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.List, contentDescription = null) },
                            label = { Text("Catatan") },
                            selected = currentDestination?.hierarchy?.any { it.route == "notes_list" } == true,
                            onClick = {
                                navController.navigate("notes_list") {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = lightPink
                            )
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Person, contentDescription = null) },
                            label = { Text("Profile") },
                            selected = currentDestination?.hierarchy?.any { it.route == "profile" } == true,
                            onClick = {
                                navController.navigate("profile") {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = lightPink
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "notes_list",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("notes_list") {
                    NotesScreen(
                        viewModel = viewModel,
                        onAddNote = { navController.navigate("add_edit_note") },
                        onSettingsClick = { navController.navigate("settings") },
                        onNoteClick = { note ->
                            navController.navigate("add_edit_note?noteId=${note.id}")
                        }
                    )
                }
                composable("profile") {
                    ProfileScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
                composable(
                    route = "add_edit_note?noteId={noteId}",
                    arguments = listOf(navArgument("noteId") { type = NavType.LongType; defaultValue = -1L })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getLong("noteId") ?: -1L
                    val note = viewModel.notes.value.find { it.id == noteId }
                    AddEditNoteScreen(
                        note = note,
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable("settings") {
                    SettingsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
                }
            }
        }
    }
}