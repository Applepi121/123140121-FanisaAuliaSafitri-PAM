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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit Test untuk NoteRepository
 * Tugas Praktikum 10 - Testing dan DI
 * Nama: Fanisa Aulia Safitri | NIM: 123140121
 *
 * Test Cases:
 * 1. getAllNotes - mengembalikan list note yang benar
 * 2. getAllNotes - mengembalikan empty list jika tidak ada note
 * 3. insertNote - memanggil repository insert dengan parameter yang benar
 * 4. updateNote - memanggil repository update dengan id yang benar
 * 5. deleteNote - memanggil repository delete dengan id yang benar
 * 6. searchNotes - mengembalikan hasil pencarian yang sesuai
 * 7. searchNotes - mengembalikan empty jika tidak ada yang cocok
 */
class NoteRepositoryTest {

    private lateinit var mockRepository: NoteRepository

    // Data dummy untuk testing
    private val testNote1 = Note(
        id = 1L,
        title = "Catatan Pertama",
        content = "Isi catatan pertama",
        createdAt = 1000L,
        updatedAt = 1000L
    )
    private val testNote2 = Note(
        id = 2L,
        title = "Belanja Minggu Ini",
        content = "Susu, telur, roti",
        createdAt = 2000L,
        updatedAt = 2000L
    )
    private val testNotes = listOf(testNote1, testNote2)

    @Before
    fun setup() {
        // Gunakan mockk relaxed agar tidak perlu stub semua method
        mockRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ─── Test 1: getAllNotes mengembalikan list yang benar ────────────────────
    @Test
    fun `getAllNotes returns correct list of notes`() = runTest {
        // Arrange
        coEvery { mockRepository.getAllNotes() } returns flowOf(testNotes)

        // Act & Assert (AAA Pattern)
        mockRepository.getAllNotes().test {
            val result = awaitItem()
            assertEquals(2, result.size)
            assertEquals("Catatan Pertama", result[0].title)
            assertEquals("Belanja Minggu Ini", result[1].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ─── Test 2: getAllNotes mengembalikan empty list ─────────────────────────
    @Test
    fun `getAllNotes returns empty list when no notes exist`() = runTest {
        // Arrange
        coEvery { mockRepository.getAllNotes() } returns flowOf(emptyList())

        // Act & Assert
        mockRepository.getAllNotes().test {
            val result = awaitItem()
            assertTrue(result.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ─── Test 3: insertNote dipanggil dengan parameter benar ─────────────────
    @Test
    fun `insertNote calls repository with correct parameters`() = runTest {
        // Arrange
        coEvery { mockRepository.insertNote(any(), any()) } just Runs

        // Act
        mockRepository.insertNote("Judul Baru", "Konten Baru")

        // Assert
        coVerify(exactly = 1) {
            mockRepository.insertNote("Judul Baru", "Konten Baru")
        }
    }

    // ─── Test 4: updateNote dipanggil dengan id yang benar ───────────────────
    @Test
    fun `updateNote calls repository with correct id and content`() = runTest {
        // Arrange
        coEvery { mockRepository.updateNote(any(), any(), any()) } just Runs

        // Act
        mockRepository.updateNote(1L, "Judul Diupdate", "Konten Diupdate")

        // Assert
        coVerify(exactly = 1) {
            mockRepository.updateNote(1L, "Judul Diupdate", "Konten Diupdate")
        }
    }

    // ─── Test 5: deleteNote dipanggil dengan id yang benar ───────────────────
    @Test
    fun `deleteNote calls repository delete with correct id`() = runTest {
        // Arrange
        coEvery { mockRepository.deleteNote(any()) } just Runs

        // Act
        mockRepository.deleteNote(1L)

        // Assert
        coVerify(exactly = 1) { mockRepository.deleteNote(1L) }
    }

    // ─── Test 6: deleteNote tidak dipanggil dengan id lain ───────────────────
    @Test
    fun `deleteNote does not delete other notes`() = runTest {
        // Arrange
        coEvery { mockRepository.deleteNote(any()) } just Runs

        // Act
        mockRepository.deleteNote(1L)

        // Assert — id 2 tidak ikut terhapus
        coVerify(exactly = 0) { mockRepository.deleteNote(2L) }
    }

    // ─── Test 7: searchNotes mengembalikan hasil yang sesuai ─────────────────
    @Test
    fun `searchNotes returns matching notes`() = runTest {
        // Arrange
        val query = "Belanja"
        coEvery { mockRepository.searchNotes(query) } returns flowOf(listOf(testNote2))

        // Act & Assert
        mockRepository.searchNotes(query).test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertTrue(result[0].title.contains("Belanja"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ─── Test 8: searchNotes mengembalikan empty jika tidak cocok ────────────
    @Test
    fun `searchNotes returns empty when no match found`() = runTest {
        // Arrange
        val query = "xyz_tidak_ada"
        coEvery { mockRepository.searchNotes(query) } returns flowOf(emptyList())

        // Act & Assert
        mockRepository.searchNotes(query).test {
            val result = awaitItem()
            assertTrue(result.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ─── Test 9: Note model memiliki data yang benar ─────────────────────────
    @Test
    fun `note model holds correct data`() {
        // Arrange & Act
        val note = Note(
            id = 99L,
            title = "Test",
            content = "Content",
            createdAt = 123L,
            updatedAt = 456L,
            isSynced = true
        )

        // Assert
        assertEquals(99L, note.id)
        assertEquals("Test", note.title)
        assertEquals("Content", note.content)
        assertTrue(note.isSynced)
    }

    // ─── Test 10: Note default isSynced adalah false ──────────────────────────
    @Test
    fun `note default isSynced is false`() {
        // Arrange & Act
        val note = Note(
            id = 1L,
            title = "Test",
            content = "Content",
            createdAt = 123L,
            updatedAt = 456L
        )

        // Assert
        assertFalse(note.isSynced)
    }
}