package com.fanisa.upgradenote.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fanisa.upgradenote.presentation.viewmodel.NotesViewModel
import org.jetbrains.compose.resources.painterResource

import upgradenote.composeapp.generated.resources.Res
import upgradenote.composeapp.generated.resources.profile_photo

private val MaroonRed = Color(0xFF800000)
private val SoftPink = Color(0xFFFFF0F5)
private val GirlyPink = Color(0xFFFFC0CB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: NotesViewModel,
    onNavigateBack: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var nameState by remember { mutableStateOf("Fanisa Aulia Safitri") }
    var nimState by remember { mutableStateOf("123140121") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftPink)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(MaroonRed),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(Res.drawable.profile_photo),
                    contentDescription = "Foto Fanisa",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isEditing) {
                    OutlinedTextField(
                        value = nameState,
                        onValueChange = { nameState = it },
                        textStyle = LocalTextStyle.current.copy(color = Color.White, textAlign = TextAlign.Center),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.padding(horizontal = 32.dp).height(60.dp)
                    )
                } else {
                    Text(
                        text = nameState,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
            }

            IconButton(
                onClick = { isEditing = !isEditing },
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)
            ) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Data Akademik", style = MaterialTheme.typography.titleSmall, color = MaroonRed)
                Spacer(modifier = Modifier.height(16.dp))

                ProfileEditRow(label = "NIM", value = nimState, isEditing = isEditing, onValueChange = { nimState = it })
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftPink)
                ProfileInfoRow(label = "Prodi", value = "Informatika")
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftPink)
                ProfileInfoRow(label = "Instansi", value = "ITERA")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ColorLens, contentDescription = null, tint = MaroonRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pilih Tema Aplikasi", style = MaterialTheme.typography.titleSmall, color = MaroonRed)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ThemeColorCircle(color = MaroonRed, label = "Maroon", isSelected = true) {
                        viewModel.changeTheme("maroon")
                    }
                    ThemeColorCircle(color = Color.Black, label = "Black", isSelected = false) {
                        viewModel.changeTheme("black")
                    }
                    ThemeColorCircle(color = GirlyPink, label = "Pink", isSelected = false) {
                        viewModel.changeTheme("pink")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontWeight = FontWeight.Medium)
        Text(value, color = MaroonRed, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProfileEditRow(label: String, value: String, isEditing: Boolean, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray, fontWeight = FontWeight.Medium)
        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.width(160.dp).height(50.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaroonRed),
                singleLine = true
            )
        } else {
            Text(value, color = MaroonRed, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ThemeColorCircle(color: Color, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color)
                .clickable { onClick() }
                .border(2.dp, if (isSelected) Color.Gray else Color.Transparent, CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}