package com.fanisa.upgradenote

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fanisa.upgradenote.data.database.DatabaseDriverFactory
import com.fanisa.upgradenote.data.repository.NoteRepository
import com.fanisa.upgradenote.presentation.viewmodel.NotesViewModel
import com.fanisa.upgradenote.data.settings.SettingsManager
import com.russhwolf.settings.SharedPreferencesSettings
import com.fanisa.upgradenote.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val driverFactory = DatabaseDriverFactory(this)
        val repository = NoteRepository(driverFactory)

        // Inisialisasi menggunakan paket 'settings' yang baru
        val sharedPrefs = getSharedPreferences("upgrade_note_prefs", Context.MODE_PRIVATE)
        val settings = SharedPreferencesSettings(sharedPrefs)

        val settingsManager = SettingsManager(settings)
        val viewModel = NotesViewModel(repository, settingsManager)

        setContent {
            // Memanggil UI dengan tema Maroon kebanggaanmu
            App(viewModel = viewModel)
        }
    }
}