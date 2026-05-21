package com.fanisa.upgradenote.di

import android.content.Context
import com.fanisa.upgradenote.data.database.DatabaseDriverFactory
import com.fanisa.upgradenote.data.platform.DeviceInfo
import com.fanisa.upgradenote.data.platform.NetworkMonitor
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseDriverFactory(androidContext()) }
    single { DeviceInfo() }
    single { NetworkMonitor(androidContext()) }

    single<Settings> {
        val sharedPrefs = androidContext().getSharedPreferences("upgrade_note_prefs", Context.MODE_PRIVATE)
        SharedPreferencesSettings(sharedPrefs)
    }
}