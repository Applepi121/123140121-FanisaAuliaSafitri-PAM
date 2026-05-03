package com.fanisa.upgradenote.di

import com.fanisa.upgradenote.data.ai.AIRepository
import com.fanisa.upgradenote.data.ai.GeminiService
import com.fanisa.upgradenote.data.repository.NoteRepository
import com.fanisa.upgradenote.data.settings.SettingsManager
import com.fanisa.upgradenote.presentation.viewmodel.NotesViewModel
import com.fanisa.upgradenote.presentation.viewmodel.NutritionViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule = module {
    // Notes
    single<NoteRepository> { NoteRepository(get()) }
    single<SettingsManager> { SettingsManager(get()) }
    factory<NotesViewModel> { NotesViewModel(get(), get()) }

    // AI / Gemini
    single<GeminiService> { GeminiService() }
    single<AIRepository> { AIRepository(get()) }
    factory<NutritionViewModel> { NutritionViewModel(get()) }
}

expect val platformModule: Module