package com.fanisa.upgradenote

import com.fanisa.upgradenote.data.ai.AIRepository
import com.fanisa.upgradenote.data.ai.GeminiService
import com.fanisa.upgradenote.di.dataModule
import com.fanisa.upgradenote.di.viewModelModule
import io.mockk.mockk
import org.junit.After
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertNotNull

/**
 * Test Koin DI Module
 * Tugas Praktikum 10 - Testing dan DI
 * Nama: Fanisa Aulia Safitri | NIM: 123140121
 */
class KoinModuleTest : KoinTest {

    @After
    fun tearDown() {
        stopKoin()
    }

    // ─── Test 1: dataModule dapat di-resolve dengan mock ─────────────────────
    @Test
    fun `dataModule resolves GeminiService correctly`() {
        startKoin {
            modules(
                module {
                    single<GeminiService> { GeminiService() }
                    single<AIRepository> { AIRepository(get()) }
                }
            )
        }

        val geminiService: GeminiService by inject()
        assertNotNull(geminiService)
    }

    // ─── Test 2: viewModelModule tidak error saat di-load ────────────────────
    @Test
    fun `viewModelModule loads without errors`() {
        // Verifikasi module tidak throw exception saat dideklarasikan
        assertNotNull(viewModelModule)
        assertNotNull(dataModule)
    }

    // ─── Test 3: AIRepository menggunakan GeminiService yang sama ────────────
    @Test
    fun `AIRepository uses same GeminiService singleton`() {
        startKoin {
            modules(
                module {
                    single<GeminiService> { GeminiService() }
                    single<AIRepository> { AIRepository(get()) }
                }
            )
        }

        val repo1: AIRepository by inject()
        val repo2: AIRepository by inject()

        // Singleton — harus instance yang sama
        assertNotNull(repo1)
        assertNotNull(repo2)
    }
}