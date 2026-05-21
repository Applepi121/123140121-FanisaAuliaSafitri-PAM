package com.fanisa.upgradenote.di

import com.fanisa.upgradenote.data.ai.AIRepository
import com.fanisa.upgradenote.data.ai.GeminiService
import com.fanisa.upgradenote.data.repository.NoteRepository
import com.fanisa.upgradenote.data.settings.SettingsManager
import com.fanisa.upgradenote.presentation.viewmodel.NotesViewModel
import com.fanisa.upgradenote.presentation.viewmodel.NutritionViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module {
    single<NoteRepository> { NoteRepository(get()) }
    single<SettingsManager> { SettingsManager(get()) }
    single<GeminiService> { GeminiService() }
    single<AIRepository> { AIRepository(get()) }
}

val viewModelModule = module {
    factory<NotesViewModel> { NotesViewModel(get(), get()) }
    factory<NutritionViewModel> { NutritionViewModel(get()) }
}

val commonModule: List<Module> = listOf(dataModule, viewModelModule)

expect val platformModule: Module