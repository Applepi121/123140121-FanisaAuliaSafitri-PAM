package com.fanisa.profileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fanisa.profileapp.data.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun updateName(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun updateBio(newBio: String) {
        _uiState.update { it.copy(bio = newBio) }
    }

    fun toggleDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(isDarkMode = enabled) }
    }
}