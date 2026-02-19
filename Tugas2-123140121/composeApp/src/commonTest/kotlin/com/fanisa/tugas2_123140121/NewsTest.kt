package com.fanisa.tugas2_123140121

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NewsTest {

    // 1. Test Fitur 4: StateFlow Counter
    @Test
    fun testReadCounterStateFlow() = runTest {
        val viewModel = NewsViewModel()
        assertEquals(0, viewModel.readCount.value)
        viewModel.incrementRead()
        assertEquals(1, viewModel.readCount.value)
    }

    // 2. Test Fitur 2 & 3: Flow Stream, Filter, dan Transformasi
    @Test
    fun testTrendingFlowLogic() = runTest {
        val viewModel = NewsViewModel()
        val firstTitle = viewModel.trendingFeed.first()

        // Cek Transformasi Uppercase (Fitur 3)
        assertEquals(firstTitle, firstTitle.uppercase())

        // Cek Filter Teknologi (Fitur 2)
        assertTrue(firstTitle.contains("AI") || firstTitle.contains("KOTLIN") || firstTitle.contains("KMP"))
    }

    // 3. Test Fitur 5: Coroutines Asynchronous (Async/Await)
    @Test
    fun testAsyncDetailFetching() = runTest {
        val repository = NewsRepository()
        val detailResult = repository.getDetailAsync(1) // Menjalankan async/await
        assertTrue(detailResult.contains("berita #1"))
    }

    // 4. Test Bonus: Error Handling dengan Catch
    @Test
    fun testFlowErrorHandling() = runTest {
        // Membuat flow simulasi yang sengaja error
        val errorFlow = flow<String> {
            throw Exception("Network Failure")
        }.catch { e ->
            emit("Error: ${e.message}")
        }

        val result = errorFlow.first()
        assertTrue(result.contains("Error: Network Failure"))
    }
}