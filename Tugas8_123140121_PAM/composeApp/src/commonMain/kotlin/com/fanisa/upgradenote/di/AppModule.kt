package com.fanisa.upgradenote.di

import com.fanisa.upgradenote.data.repository.NoteRepository
import com.fanisa.upgradenote.data.settings.SettingsManager
import com.fanisa.upgradenote.presentation.viewmodel.NotesViewModel
import org.koin.dsl.module
import org.koin.core.module.Module

val commonModule = module {
    single { NoteRepository(get()) }
    single { SettingsManager(get()) }
    factory { NotesViewModel(get(), get()) }
}

expect val platformModule: Module