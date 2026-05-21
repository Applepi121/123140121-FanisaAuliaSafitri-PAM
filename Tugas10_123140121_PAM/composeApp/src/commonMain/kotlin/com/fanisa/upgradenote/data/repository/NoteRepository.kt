package com.fanisa.upgradenote.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fanisa.upgradenote.data.database.DatabaseDriverFactory
import com.fanisa.upgradenote.db.NotesDatabase
import com.fanisa.upgradenote.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class NoteRepository(driverFactory: DatabaseDriverFactory) {
    private val database = NotesDatabase(driverFactory.createDriver())
    private val queries = database.noteQueries

    fun getAllNotes(): Flow<List<Note>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { it.toNote() }
            }
    }

    suspend fun insertNote(title: String, content: String) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        queries.insert(
            title = title,
            content = content,
            created_at = currentTime,
            updated_at = currentTime,
            is_synced = 0L
        )
    }

    suspend fun updateNote(id: Long, title: String, content: String) {
        queries.update(
            title = title,
            content = content,
            updated_at = Clock.System.now().toEpochMilliseconds(),
            id = id
        )
    }

    fun deleteNote(id: Long) {
        queries.delete(id)
    }

    fun searchNotes(query: String): Flow<List<Note>> {
        return queries.search(query, query)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { it.toNote() }
            }
    }
}

fun com.fanisa.upgradenote.db.NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAt = created_at,
        updatedAt = updated_at,
        isSynced = is_synced != 0L
    )
}