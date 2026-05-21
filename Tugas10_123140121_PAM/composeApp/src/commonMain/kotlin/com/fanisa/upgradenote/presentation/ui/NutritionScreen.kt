package com.fanisa.upgradenote.presentation.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fanisa.upgradenote.data.ai.NutritionInfo
import com.fanisa.upgradenote.presentation.viewmodel.NutritionViewModel

private val MaroonRed = Color(0xFF800000)
private val LightPink = Color(0xFFFFE4E1)

private val quickFoods = listOf(
    "Nasi Goreng", "Ayam Goreng", "Rendang", "Gado-gado",
    "Soto Ayam", "Mie Goreng", "Pisang", "Apel"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(
    viewModel: NutritionViewModel,
    onNavigateBack: () -> Unit,
    onOpenCamera: (() -> Unit)? = null,
    onOpenGallery: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    var foodInput by remember { mutableStateOf("") }
    var portionAmount by remember { mutableStateOf("1") }
    var portionUnit by remember { mutableStateOf("porsi") }
    var unitExpanded by remember { mutableStateOf(false) }

    val units = listOf("porsi", "gram", "cup", "sendok makan", "buah", "mangkok", "piring")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Analisis Nutrisi AI",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaroonRed)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ─── Tab Teks / Kamera ────────────────────────────────────────────
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaroonRed,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaroonRed
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0; viewModel.reset() },
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    text = { Text("Cari Makanan") },
                    selectedContentColor = MaroonRed,
                    unselectedContentColor = Color.Gray
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1; viewModel.reset() },
                    icon = { Icon(Icons.Default.CameraAlt, contentDescription = null) },
                    text = { Text("Foto Makanan") },
                    selectedContentColor = MaroonRed,
                    unselectedContentColor = Color.Gray
                )
            }

            // ─── Panel Input Teks ─────────────────────────────────────────────
            AnimatedVisibility(visible = selectedTab == 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            "Nama Makanan (Bahasa Apapun)",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(6.dp))

                        OutlinedTextField(
                            value = foodInput,
                            onValueChange = { foodInput = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Nasi Goreng / Fried Rice / 炒饭 / ...") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaroonRed,
                                focusedLabelColor = MaroonRed,
                                cursorColor = MaroonRed
                            )
                        )

                        Text(
                            "💡 Mendukung semua bahasa: Indonesia, Inggris, Jepang, dll.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1565C0),
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(Modifier.height(10.dp))

                        // Porsi & Satuan
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = portionAmount,
                                onValueChange = {
                                    portionAmount = it.filter { c -> c.isDigit() || c == '.' }
                                },
                                modifier = Modifier.width(90.dp),
                                label = { Text("Porsi") },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaroonRed,
                                    focusedLabelColor = MaroonRed,
                                    cursorColor = MaroonRed
                                )
                            )
                            ExposedDropdownMenuBox(
                                expanded = unitExpanded,
                                onExpandedChange = { unitExpanded = !unitExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = portionUnit,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Satuan") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = unitExpanded
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaroonRed,
                                        focusedLabelColor = MaroonRed
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = unitExpanded,
                                    onDismissRequest = { unitExpanded = false }
                                ) {
                                    units.forEach { unit ->
                                        DropdownMenuItem(
                                            text = { Text(unit) },
                                            onClick = {
                                                portionUnit = unit
                                                unitExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        // Quick Picks
                        Text(
                            "Pilihan cepat:",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(4.dp))
                        QuickPickChips(
                            items = quickFoods,
                            onSelect = { foodInput = it }
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.reset()
                                viewModel.analyzeFood(
                                    foodInput.trim(),
                                    portionAmount.ifBlank { "1" },
                                    portionUnit
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            enabled = foodInput.isNotBlank() && !uiState.isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = MaroonRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Analisis Nutrisi",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // ─── Panel Kamera ─────────────────────────────────────────────────
            AnimatedVisibility(visible = selectedTab == 1) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(LightPink),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = MaroonRed,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Text(
                            "Foto Makanan Anda",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaroonRed
                        )

                        Text(
                            "Ambil foto makanan dan AI akan mendeteksi jenis makanan " +
                                    "serta menghitung kandungan gizinya secara otomatis.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = {
                                viewModel.reset()
                                onOpenCamera?.invoke()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            enabled = !uiState.isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = MaroonRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Buka Kamera",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.reset()
                                onOpenGallery?.invoke()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            enabled = !uiState.isLoading,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaroonRed
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Photo,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Pilih dari Galeri")
                        }
                    }
                }
            }

            // ─── Loading ──────────────────────────────────────────────────────
            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LoadingCard(message = uiState.loadingMessage)
            }

            // ─── Error ────────────────────────────────────────────────────────
            AnimatedVisibility(
                visible = uiState.errorTitle != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                uiState.errorTitle?.let { title ->
                    ErrorCard(
                        title = title,
                        message = uiState.errorMessage ?: "",
                        hint = uiState.errorHint ?: "",
                        cooldownSeconds = uiState.cooldownSeconds,
                        onRetry = {
                            viewModel.clearError()
                            if (selectedTab == 0) {
                                viewModel.analyzeFood(
                                    foodInput.trim(),
                                    portionAmount.ifBlank { "1" },
                                    portionUnit
                                )
                            } else {
                                onOpenCamera?.invoke()
                            }
                        }
                    )
                }
            }

            // ─── Result ───────────────────────────────────────────────────────
            AnimatedVisibility(
                visible = uiState.result != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                uiState.result?.let { nutrition ->
                    NutritionResultSection(
                        nutrition = nutrition,
                        onAnalyzeAnother = {
                            viewModel.reset()
                            foodInput = ""
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ─── Quick Picks ──────────────────────────────────────────────────────────────

@Composable
private fun QuickPickChips(items: List<String>, onSelect: (String) -> Unit) {
    // Pakai FlowRow agar chip tidak terpotong di layar sempit
    androidx.compose.foundation.layout.FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { item ->
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = LightPink,
                modifier = Modifier.clickable { onSelect(item) }
            ) {
                Text(
                    item,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaroonRed,
                    maxLines = 1
                )
            }
        }
    }
}

// ─── Loading Card ─────────────────────────────────────────────────────────────

@Composable
private fun LoadingCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(3) { index ->
                    val transition = rememberInfiniteTransition(label = "dot$index")
                    val alpha by transition.animateFloat(
                        initialValue = 0.2f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600),
                            repeatMode = RepeatMode.Reverse,
                            initialStartOffset = StartOffset(index * 200)
                        ),
                        label = "alpha$index"
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(MaroonRed.copy(alpha = alpha))
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaroonRed,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                "Gemini AI sedang memproses...",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ─── Error Card ───────────────────────────────────────────────────────────────

@Composable
private fun ErrorCard(
    title: String,
    message: String,
    hint: String,
    cooldownSeconds: Int = 0,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFC62828),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFFC62828),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                message,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFB71C1C)
            )
            if (hint.isNotBlank()) {
                Text(
                    hint,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB71C1C).copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Tampilkan countdown jika sedang cooldown rate limit
            if (cooldownSeconds > 0) {
                Spacer(Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFCDD2)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = null,
                            tint = Color(0xFFC62828),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Auto-retry dalam $cooldownSeconds detik...",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFC62828),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                Spacer(Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onRetry,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFC62828)
                    )
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Coba Lagi")
                }
            }
        }
    }
}

// ─── Result Section ───────────────────────────────────────────────────────────

@Composable
private fun NutritionResultSection(
    nutrition: NutritionInfo,
    onAnalyzeAnother: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        // Header makanan
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(nutrition.emoji, fontSize = 36.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        nutrition.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaroonRed
                    )
                    Text(
                        "${nutrition.portion} · estimasi nilai gizi",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        // Kalori banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LightPink),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Total Kalori",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                    Text(
                        "${nutrition.calories}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaroonRed
                    )
                    Text(
                        "kkal per porsi",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                CalorieBadge(calories = nutrition.calories)
            }
        }

        // Makronutrien
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MacroCard(
                label = "Karbo",
                value = nutrition.carbs,
                unit = "g",
                color = Color(0xFF388E3C),
                modifier = Modifier.weight(1f)
            )
            MacroCard(
                label = "Protein",
                value = nutrition.protein,
                unit = "g",
                color = Color(0xFF1565C0),
                modifier = Modifier.weight(1f)
            )
            MacroCard(
                label = "Lemak",
                value = nutrition.fat,
                unit = "g",
                color = Color(0xFFF57F17),
                modifier = Modifier.weight(1f)
            )
        }

        // Mikronutrien
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SectionLabel("Mikronutrien")
                Spacer(Modifier.height(8.dp))
                val microItems = listOf(
                    "Serat"      to "${String.format("%.1f", nutrition.fiber)} g",
                    "Gula"       to "${String.format("%.1f", nutrition.sugar)} g",
                    "Sodium"     to "${nutrition.sodium} mg",
                    "Kolesterol" to "${nutrition.cholesterol} mg"
                )
                microItems.forEachIndexed { index, (label, value) ->
                    MicroRow(label, value)
                    if (index < microItems.lastIndex) {
                        HorizontalDivider(color = Color(0xFFF5F5F5), thickness = 0.5.dp)
                    }
                }
            }
        }

        // Health Score
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SectionLabel("Health Score")
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HealthScoreCircle(score = nutrition.healthScore)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            nutrition.healthCategory,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = healthScoreColor(nutrition.healthScore)
                        )
                        Text(
                            nutrition.scoreExplanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Saran Gizi
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Color(0xFF1565C0),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Saran Gizi",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF1565C0),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(8.dp))
                nutrition.tips.forEach { tip ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            "— ",
                            color = Color(0xFF1565C0).copy(alpha = 0.6f),
                            fontSize = 13.sp
                        )
                        Text(
                            tip,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF0D47A1)
                        )
                    }
                }
            }
        }

        // Tombol analisis lain
        OutlinedButton(
            onClick = onAnalyzeAnother,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaroonRed)
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Analisis Makanan Lain")
        }
    }
}

// ─── Komponen kecil ───────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray,
        letterSpacing = 1.sp
    )
}

@Composable
private fun MacroCard(
    label: String,
    value: Double,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Spacer(Modifier.height(4.dp))
            Text(
                String.format("%.1f", value),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(unit, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Composable
private fun MicroRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CalorieBadge(calories: Int) {
    val (text, color) = when {
        calories < 200 -> "Rendah Kalori" to Color(0xFF4CAF50)
        calories < 500 -> "Sedang"        to Color(0xFFFFC107)
        else           -> "Tinggi Kalori" to Color(0xFFF44336)
    }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun HealthScoreCircle(score: Int) {
    val color = healthScoreColor(score)
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "$score/10",
            fontWeight = FontWeight.Bold,
            color = color,
            fontSize = 14.sp
        )
    }
}

private fun healthScoreColor(score: Int): Color = when {
    score >= 7 -> Color(0xFF2E7D32)
    score >= 4 -> Color(0xFFF57F17)
    else       -> Color(0xFFC62828)
}