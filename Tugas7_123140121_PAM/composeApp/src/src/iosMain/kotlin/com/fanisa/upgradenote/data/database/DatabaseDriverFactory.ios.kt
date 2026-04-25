package com.fanisa.upgradenote.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.itera.notes.db.NotesDatabase

/**
 * Implementasi DatabaseDriverFactory untuk iOS
 * Menggunakan NativeSqliteDriver
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = NotesDatabase.Schema,
            name = "notes.db"
        )
    }
}