package com.fanisa.upgradenote

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fanisa.upgradenote.data.platform.NetworkMonitor
import com.fanisa.upgradenote.presentation.ui.AddEditNoteScreen
import com.fanisa.upgradenote.presentation.ui.NotesScreen
import com.fanisa.upgradenote.presentation.ui.NutritionScreen
import com.fanisa.upgradenote.presentation.ui.ProfileScreen
import com.fanisa.upgradenote.presentation.ui.SettingsScreen
import com.fanisa.upgradenote.presentation.viewmodel.NotesViewModel
import com.fanisa.upgradenote.presentation.viewmodel.NutritionViewModel
import org.koin.compose.koinInject

@Composable
fun App(
    viewModel: NotesViewModel,
    nutritionViewModel: NutritionViewModel? = null,
    onOpenCamera: (() -> Unit)? = null,
    onOpenGallery: (() -> Unit)? = null
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentTheme by viewModel.theme.collectAsState()
    val networkMonitor: NetworkMonitor = koinInject()
    val isConnected by networkMonitor.observeConnectivity().collectAsState(initial = true)

    // Gunakan parameter jika ada, fallback ke koinInject
    val resolvedNutritionVm: NutritionViewModel =
        nutritionViewModel ?: koinInject()

    val maroon    = Color(0xFF800000)
    val lightPink = Color(0xFFFFE4E1)
    val girlyPink = Color(0xFFFFC0CB)

    val colorScheme = when (currentTheme) {
        "pink" -> lightColorScheme(
            primary   = girlyPink,
            onPrimary = Color.White,
            secondary = Color.Black,
            surface   = Color(0xFFFFF0F5)
        )
        "black" -> darkColorScheme(
            primary   = Color.White,
            onPrimary = Color.Black,
            secondary = Color.Gray,
            surface   = Color.Black
        )
        else -> lightColorScheme(
            primary   = maroon,
            onPrimary = Color.White,
            secondary = Color.Black,
            surface   = Color.White
        )
    }

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            topBar = {
                Column {
                    AnimatedVisibility(visible = !isConnected) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF800000)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.CloudOff,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Mode Offline: Periksa Koneksi Internet",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            },
            bottomBar = {
                val showBottomBar = currentDestination?.route in listOf(
                    "notes_list", "nutrition", "profile"
                )
                if (showBottomBar) {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.List,
                                    contentDescription = null
                                )
                            },
                            label = { Text("Catatan") },
                            selected = currentDestination?.hierarchy
                                ?.any { it.route == "notes_list" } == true,
                            onClick = {
                                navController.navigate("notes_list") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                indicatorColor    = lightPink
                            )
                        )
                        NavigationBarItem(
                            icon = {
                                Icon(Icons.Default.Restaurant, contentDescription = null)
                            },
                            label = { Text("Nutrisi") },
                            selected = currentDestination?.hierarchy
                                ?.any { it.route == "nutrition" } == true,
                            onClick = {
                                navController.navigate("nutrition") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                indicatorColor    = lightPink
                            )
                        )
                        NavigationBarItem(
                            icon = {
                                Icon(Icons.Default.Person, contentDescription = null)
                            },
                            label = { Text("Profile") },
                            selected = currentDestination?.hierarchy
                                ?.any { it.route == "profile" } == true,
                            onClick = {
                                navController.navigate("profile") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                indicatorColor    = lightPink
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
                composable("nutrition") {
                    NutritionScreen(
                        viewModel = resolvedNutritionVm,
                        onNavigateBack = { navController.popBackStack() },
                        onOpenCamera = onOpenCamera,
                        onOpenGallery = onOpenGallery
                    )
                }
                composable("profile") {
                    ProfileScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route = "add_edit_note?noteId={noteId}",
                    arguments = listOf(
                        navArgument("noteId") {
                            type = NavType.LongType
                            defaultValue = -1L
                        }
                    )
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
                    SettingsScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}