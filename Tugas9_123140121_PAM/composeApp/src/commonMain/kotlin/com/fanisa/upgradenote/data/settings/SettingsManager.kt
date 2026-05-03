package com.fanisa.upgradenote.data.settings

import com.russhwolf.settings.Settings

class SettingsManager(private val settings: Settings) {
    companion object {
        private const val KEY_THEME = "app_theme"
        private const val KEY_SORT_BY_DATE = "sort_by_date"
    }

    var theme: String
        get() = settings.getString(KEY_THEME, "system")
        set(value) {
            settings.putString(KEY_THEME, value)
        }

    var isSortDescending: Boolean
        get() = settings.getBoolean(KEY_SORT_BY_DATE, true)
        set(value) {
            settings.putBoolean(KEY_SORT_BY_DATE, value)
        }
}