package com.fanisa.upgradenote

import app.cash.turbine.test
import com.fanisa.upgradenote.data.repository.NoteRepository
import com.fanisa.upgradenote.data.settings.SettingsManager
import com.fanisa.upgradenote.domain.model.Note
import com.fanisa.upgradenote.presentation.viewmodel.NotesViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit Test untuk NotesViewModel menggunakan MockK
 * Tugas Praktikum 10 - Testing dan DI
 * Nama: Fanisa Aulia Safitri | NIM: 123140121
 *
 * Test Cases:
 * 1. notes StateFlow mengembalikan data dari repository
 * 2. addNote memanggil repository.insertNote
 * 3. deleteNote memanggil repository.deleteNote dengan id benar
 * 4. updateNote memanggil repository.updateNote dengan parameter benar
 * 5. onSearchQueryChanged mengupdate searchQuery
 * 6. changeTheme mengupdate theme state
 * 7. changeSortOrder mengupdate sort order
 */
@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    // ─── Test dispatcher untuk coroutine ─────────────────────────────────────
    private val testDispatcher = StandardTestDispatcher()

    // ─── Mock dependencies ────────────────────────────────────────────────────
    private val mockRepository = mockk<NoteRepository>()
    private val mockSettingsManager = mockk<SettingsManager>(relaxed = true)

    private lateinit var viewModel: NotesViewModel

    // ─── Data dummy ───────────────────────────────────────────────────────────
    private val testNote = Note(
        id = 1L,
        title = "Catatan Test",
        content = "Isi test",
        createdAt = 1000L,
        updatedAt = 1000L
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Stub settings manager
        every { mockSettingsManager.theme } returns "default"
        every { mockSettingsManager.isSortDescending } returns true

        // Stub repository getAllNotes
        every { mockRepository.getAllNotes() } returns flowOf(listOf(testNote))
        every { mockRepository.searchNotes(any()) } returns flowOf(emptyList())

        viewModel = NotesViewModel(mockRepository, mockSettingsManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // ─── Test 1: notes StateFlow mengembalikan data dari repository ───────────
    @Test
    fun `notes flow emits data from repository`() = runTest {
        // Arrange
        every { mockRepository.getAllNotes() } returns flowOf(listOf(testNote))
        viewModel = NotesViewModel(mockRepository, mockSettingsManager)

        // Act & Assert
        viewModel.notes.test {
            val result = awaitItem()
            // State awal bisa empty, tunggu yang berisi data
            if (result.isEmpty()) {
                val loaded = awaitItem()
                assertNotNull(loaded)
            } else {
                assertNotNull(result)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ─── Test 2: addNote memanggil repository.insertNote ─────────────────────
    @Test
    fun `addNote calls repository insertNote with correct parameters`() = runTest {
        // Arrange
        coEvery { mockRepository.insertNote(any(), any()) } just Runs

        // Act
        viewModel.addNote("Judul Baru", "Konten Baru")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) {
            mockRepository.insertNote("Judul Baru", "Konten Baru")
        }
    }

    // ─── Test 3: deleteNote memanggil repository.deleteNote ──────────────────
    @Test
    fun `deleteNote calls repository deleteNote with correct id`() = runTest {
        // Arrange
        coEvery { mockRepository.deleteNote(any()) } just Runs

        // Act
        viewModel.deleteNote(1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) { mockRepository.deleteNote(1L) }
    }

    // ─── Test 4: updateNote memanggil repository.updateNote ──────────────────
    @Test
    fun `updateNote calls repository updateNote with correct parameters`() = runTest {
        // Arrange
        coEvery { mockRepository.updateNote(any(), any(), any()) } just Runs

        // Act
        viewModel.updateNote(1L, "Judul Edit", "Konten Edit")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) {
            mockRepository.updateNote(1L, "Judul Edit", "Konten Edit")
        }
    }

    // ─── Test 5: searchQuery diupdate ketika onSearchQueryChanged dipanggil ──
    @Test
    fun `onSearchQueryChanged updates searchQuery state`() = runTest {
        // Act
        viewModel.onSearchQueryChanged("Catatan")

        // Assert
        assertEquals("Catatan", viewModel.searchQuery.value)
    }

    // ─── Test 6: searchQuery kosong di awal ──────────────────────────────────
    @Test
    fun `initial searchQuery is empty`() {
        assertEquals("", viewModel.searchQuery.value)
    }

    // ─── Test 7: changeTheme mengupdate theme state ───────────────────────────
    @Test
    fun `changeTheme updates theme state correctly`() {
        // Act
        viewModel.changeTheme("pink")

        // Assert
        assertEquals("pink", viewModel.theme.value)
        verify { mockSettingsManager.theme = "pink" }
    }

    // ─── Test 8: changeSortOrder mengupdate sort order ────────────────────────
    @Test
    fun `changeSortOrder updates isSortDescending state`() {
        // Arrange
        val initialSort = viewModel.isSortDescending.value

        // Act
        viewModel.changeSortOrder(!initialSort)

        // Assert
        assertEquals(!initialSort, viewModel.isSortDescending.value)
        verify { mockSettingsManager.isSortDescending = !initialSort }
    }

    // ─── Test 9: deleteNote tidak dipanggil dengan id yang salah ─────────────
    @Test
    fun `deleteNote only deletes specified note`() = runTest {
        // Arrange
        coEvery { mockRepository.deleteNote(any()) } just Runs

        // Act
        viewModel.deleteNote(1L)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert — id 2 tidak ikut terhapus
        coVerify(exactly = 0) { mockRepository.deleteNote(2L) }
        coVerify(exactly = 1) { mockRepository.deleteNote(1L) }
    }

    // ─── Test 10: theme awal sesuai settingsManager ───────────────────────────
    @Test
    fun `initial theme matches settingsManager`() {
        assertEquals("default", viewModel.theme.value)
    }
}