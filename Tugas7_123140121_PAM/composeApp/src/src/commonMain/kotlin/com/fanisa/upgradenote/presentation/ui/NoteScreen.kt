package com.fanisa.upgradenote.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fanisa.upgradenote.domain.model.Note
import com.fanisa.upgradenote.presentation.viewmodel.NotesViewModel

private val MaroonRed = Color(0xFF800000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    viewModel: NotesViewModel,
    onAddNote: () -> Unit,
    onSettingsClick: () -> Unit,
    onNoteClick: (Note) -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UpgradeNote", color = Color.White, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaroonRed)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNote,
                containerColor = MaroonRed,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                placeholder = { Text("Cari catatan...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaroonRed,
                    focusedLabelColor = MaroonRed
                )
            )

            if (notes.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Catatan tidak ditemukan", color = Color.Gray)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(notes) { note ->
                        NoteItem(
                            note = note,
                            onClick = { onNoteClick(note) },
                            onDelete = { viewModel.deleteNote(note.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(note: Note, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(note.title, style = MaterialTheme.typography.titleMedium, color = MaroonRed, fontWeight = FontWeight.Bold)
                Text(note.content, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
            }
        }
    }
}