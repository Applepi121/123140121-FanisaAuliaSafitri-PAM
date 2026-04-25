package com.fanisa.upgradenote.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fanisa.upgradenote.data.repository.NoteRepository
import com.fanisa.upgradenote.data.settings.SettingsManager
import com.fanisa.upgradenote.domain.model.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class NotesViewModel(
    private val repository: NoteRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _theme = MutableStateFlow(settingsManager.theme)
    val theme = _theme.asStateFlow()

    private val _isSortDescending = MutableStateFlow(settingsManager.isSortDescending)
    val isSortDescending = _isSortDescending.asStateFlow()

    // Menggabungkan pencarian dan pengurutan secara reaktif
    val notes: StateFlow<List<Note>> = combine(
        _searchQuery,
        _isSortDescending
    ) { query, isDesc ->
        query to isDesc
    }
        .debounce(300L)
        .flatMapLatest { (query, isDesc) ->
            val flow = if (query.isEmpty()) repository.getAllNotes() else repository.searchNotes(query)
            flow.map { list ->
                if (isDesc) list.sortedByDescending { it.updatedAt }
                else list.sortedBy { it.updatedAt }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addNote(title: String, content: String) {
        viewModelScope.launch { repository.insertNote(title, content) }
    }

    fun updateNote(id: Long, title: String, content: String) {
        viewModelScope.launch { repository.updateNote(id, title, content) }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch { repository.deleteNote(id) }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun changeTheme(newTheme: String) {
        settingsManager.theme = newTheme
        _theme.value = newTheme
    }

    fun changeSortOrder(isDesc: Boolean) {
        settingsManager.isSortDescending = isDesc
        _isSortDescending.value = isDesc
    }
}