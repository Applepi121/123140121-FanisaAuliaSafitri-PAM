package com.fanisa.upgradenote

import app.cash.turbine.test
import com.fanisa.upgradenote.data.repository.NoteRepository
import com.fanisa.upgradenote.domain.model.Note
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Flow Test menggunakan Turbine
 * Tugas Praktikum 10 - Testing dan DI
 * Nama: Fanisa Aulia Safitri | NIM: 123140121
 *
 * Test Cases:
 * 1. getAllNotes flow mengemit list note dengan benar
 * 2. getAllNotes flow mengemit empty list
 * 3. searchNotes flow mengemit hasil pencarian yang sesuai query
 * 4. searchNotes flow mengemit empty ketika query tidak cocok
 * 5. Flow multiple emissions berurutan
 */
class NoteFlowTest {

    private val mockRepository = mockk<NoteRepository>()

    private val noteA = Note(1L, "Android Tips", "Kotlin tips", 1000L, 1000L)
    private val noteB = Note(2L, "iOS Tips", "Swift tips", 2000L, 2000L)
    private val noteC = Note(3L, "Android Studio", "Shortcuts", 3000L, 3000L)

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ─── Test 1: getAllNotes flow mengemit data yang benar ────────────────────
    @Test
    fun `getAllNotes flow emits correct notes list`() = runTest {
        // Arrange
        val expectedNotes = listOf(noteA, noteB, noteC)
        coEvery { mockRepository.getAllNotes() } returns flowOf(expectedNotes)

        // Act & Assert dengan Turbine
        mockRepository.getAllNotes().test {
            val emission = awaitItem()
            assertEquals(3, emission.size)
            assertEquals("Android Tips", emission[0].title)
            assertEquals("iOS Tips", emission[1].title)
            assertEquals("Android Studio", emission[2].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ─── Test 2: getAllNotes flow mengemit empty list ─────────────────────────
    @Test
    fun `getAllNotes flow emits empty list when no data`() = runTest {
        // Arrange
        coEvery { mockRepository.getAllNotes() } returns flowOf(emptyList())

        // Act & Assert
        mockRepository.getAllNotes().test {
            val emission = awaitItem()
            assertTrue(emission.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ─── Test 3: searchNotes flow mengemit hasil yang sesuai query ────────────
    @Test
    fun `searchNotes flow emits only matching notes`() = runTest {
        // Arrange — query "Android" hanya cocok dengan noteA dan noteC
        val query = "Android"
        val expected = listOf(noteA, noteC)
        coEvery { mockRepository.searchNotes(query) } returns flowOf(expected)

        // Act & Assert
        mockRepository.searchNotes(query).test {
            val result = awaitItem()
            assertEquals(2, result.size)
            assertTrue(result.all { it.title.contains("Android") })
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ─── Test 4: searchNotes flow mengemit empty ketika tidak ada yang cocok ──
    @Test
    fun `searchNotes flow emits empty when no match`() = runTest {
        // Arrange
        val query = "Flutter"
        coEvery { mockRepository.searchNotes(query) } returns flowOf(emptyList())

        // Act & Assert
        mockRepository.searchNotes(query).test {
            val result = awaitItem()
            assertTrue(result.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ─── Test 5: Flow mengemit satu item dan selesai ──────────────────────────
    @Test
    fun `flow completes after emitting single item`() = runTest {
        // Arrange
        coEvery { mockRepository.getAllNotes() } returns flowOf(listOf(noteA))

        // Act & Assert
        mockRepository.getAllNotes().test {
            val item = awaitItem()
            assertEquals(1, item.size)
            assertEquals(noteA.id, item[0].id)
            awaitComplete()
        }
    }

    // ─── Test 6: searchNotes dipanggil dengan query yang benar ───────────────
    @Test
    fun `searchNotes is called with exact query string`() = runTest {
        // Arrange
        val query = "Catatan Penting"
        coEvery { mockRepository.searchNotes(any()) } returns flowOf(emptyList())

        // Act
        mockRepository.searchNotes(query).test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify { mockRepository.searchNotes(query) }
        coVerify(exactly = 0) { mockRepository.searchNotes("query lain") }
    }
}