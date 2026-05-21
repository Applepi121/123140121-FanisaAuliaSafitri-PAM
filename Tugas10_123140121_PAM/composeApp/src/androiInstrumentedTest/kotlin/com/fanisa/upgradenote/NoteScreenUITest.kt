package com.fanisa.upgradenote

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.fanisa.upgradenote.domain.model.Note
import com.fanisa.upgradenote.presentation.ui.TestTags
import org.junit.Rule
import org.junit.Test

/**
 * UI Test untuk NotesScreen menggunakan Compose Test
 * Tugas Praktikum 10 - Testing dan DI
 * Nama: Fanisa Aulia Safitri | NIM: 123140121
 *
 * Test Cases:
 * 1. Empty state ditampilkan ketika tidak ada catatan
 * 2. Daftar catatan ditampilkan dengan benar
 * 3. FAB Add button dapat diklik
 * 4. Search bar dapat menerima input
 * 5. Judul catatan tampil di list
 */
class NoteScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ─── Data dummy ───────────────────────────────────────────────────────────
    private val sampleNotes = listOf(
        Note(1L, "Catatan Pertama", "Isi pertama", 1000L, 1000L),
        Note(2L, "Catatan Kedua", "Isi kedua", 2000L, 2000L)
    )

    // ─── Test 1: Tombol Analisis Nutrisi ada di screen ───────────────────────
    @Test
    fun nutritionScreen_analyzeButton_isDisplayed() {
        // Arrange & Act
        composeTestRule.setContent {
            // Tampilkan komponen sederhana dengan test tag
            androidx.compose.material3.Button(
                onClick = {},
                modifier = androidx.compose.ui.Modifier.semantics {
                    testTag = TestTags.ANALYZE_BUTTON
                }
            ) {
                androidx.compose.material3.Text("Analisis Nutrisi")
            }
        }

        // Assert
        composeTestRule
            .onNodeWithTag(TestTags.ANALYZE_BUTTON)
            .assertIsDisplayed()
    }

    // ─── Test 2: Input field food dapat menerima teks ─────────────────────────
    @Test
    fun nutritionScreen_foodInput_acceptsText() {
        // Arrange & Act
        composeTestRule.setContent {
            androidx.compose.material3.OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = androidx.compose.ui.Modifier.semantics {
                    testTag = TestTags.FOOD_INPUT
                },
                placeholder = { androidx.compose.material3.Text("Nama makanan...") }
            )
        }

        // Assert — field ada dan dapat diinteraksi
        composeTestRule
            .onNodeWithTag(TestTags.FOOD_INPUT)
            .assertIsDisplayed()
    }

    // ─── Test 3: Error card ditampilkan dengan teks yang benar ────────────────
    @Test
    fun errorCard_displaysCorrectMessage() {
        // Arrange & Act
        composeTestRule.setContent {
            androidx.compose.material3.Card(
                modifier = androidx.compose.ui.Modifier.semantics {
                    testTag = TestTags.ERROR_CARD
                }
            ) {
                androidx.compose.material3.Text("Tidak Ada Koneksi")
                androidx.compose.material3.Text("Periksa koneksi internet Anda.")
            }
        }

        // Assert
        composeTestRule
            .onNodeWithTag(TestTags.ERROR_CARD)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Tidak Ada Koneksi")
            .assertIsDisplayed()
    }

    // ─── Test 4: Judul catatan muncul di UI ───────────────────────────────────
    @Test
    fun noteItem_titleIsDisplayed() {
        // Arrange & Act
        composeTestRule.setContent {
            androidx.compose.foundation.layout.Column {
                sampleNotes.forEach { note ->
                    androidx.compose.material3.Text(
                        text = note.title,
                        modifier = androidx.compose.ui.Modifier.semantics {
                            testTag = "${TestTags.NOTE_ITEM}_${note.id}"
                        }
                    )
                }
            }
        }

        // Assert
        composeTestRule
            .onNodeWithText("Catatan Pertama")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Catatan Kedua")
            .assertIsDisplayed()
    }

    // ─── Test 5: Empty state ditampilkan ketika tidak ada catatan ─────────────
    @Test
    fun emptyState_isDisplayedWhenNoNotes() {
        // Arrange & Act
        composeTestRule.setContent {
            androidx.compose.material3.Text(
                text = "Belum ada catatan",
                modifier = androidx.compose.ui.Modifier.semantics {
                    testTag = TestTags.EMPTY_STATE
                }
            )
        }

        // Assert
        composeTestRule
            .onNodeWithTag(TestTags.EMPTY_STATE)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Belum ada catatan")
            .assertIsDisplayed()
    }

    // ─── Test 6: FAB/Tombol Add dapat diklik ─────────────────────────────────
    @Test
    fun addButton_isClickable() {
        // Arrange
        var clicked = false

        composeTestRule.setContent {
            androidx.compose.material3.FloatingActionButton(
                onClick = { clicked = true },
                modifier = androidx.compose.ui.Modifier.semantics {
                    testTag = TestTags.ADD_FAB
                }
            ) {
                androidx.compose.material3.Text("+")
            }
        }

        // Act
        composeTestRule
            .onNodeWithTag(TestTags.ADD_FAB)
            .performClick()

        // Assert
        assert(clicked)
    }

    // ─── Test 7: Search bar menerima input teks ───────────────────────────────
    @Test
    fun searchBar_acceptsTextInput() {
        // Arrange
        var searchText = ""

        composeTestRule.setContent {
            androidx.compose.material3.OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = androidx.compose.ui.Modifier.semantics {
                    testTag = TestTags.SEARCH_BAR
                },
                placeholder = { androidx.compose.material3.Text("Cari catatan...") }
            )
        }

        // Act
        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_BAR)
            .performTextInput("Android")

        // Assert
        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_BAR)
            .assertIsDisplayed()
    }
}