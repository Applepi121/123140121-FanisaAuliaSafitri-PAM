package com.fanisa.upgradenote.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fanisa.upgradenote.data.ai.AIError
import com.fanisa.upgradenote.data.ai.AIRepository
import com.fanisa.upgradenote.data.ai.NutritionInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NutritionUiState(
    val isLoading: Boolean = false,
    val result: NutritionInfo? = null,
    val errorTitle: String? = null,
    val errorMessage: String? = null,
    val errorHint: String? = null,
    val loadingMessage: String = "Menganalisis kandungan gizi...",
    val cooldownSeconds: Int = 0  // countdown saat rate limit
)

class NutritionViewModel(
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NutritionUiState())
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()

    private val textLoadingMessages = listOf(
        "Mengidentifikasi bahan makanan...",
        "Menghitung kandungan gizi...",
        "Menyusun saran kesehatan...",
        "Memproses dengan Gemini AI..."
    )

    private val imageLoadingMessages = listOf(
        "Mendeteksi makanan dari foto...",
        "Menganalisis visual makanan...",
        "Menghitung estimasi kalori...",
        "Menyusun informasi gizi..."
    )

    // ─── Analisis dari teks ───────────────────────────────────────────────────
    fun analyzeFood(foodName: String, amount: String, unit: String) {
        if (foodName.isBlank()) return
        startLoading(textLoadingMessages[0])

        viewModelScope.launch {
            val msgJob = launchLoadingMessages(textLoadingMessages)
            val result = aiRepository.analyzeFood(
                foodName.trim(),
                amount.ifBlank { "1" },
                unit
            )
            msgJob.cancel()
            handleResult(result) { analyzeFood(foodName, amount, unit) }
        }
    }

    // ─── Analisis dari foto ───────────────────────────────────────────────────
    fun analyzeFoodFromImage(base64Image: String, mimeType: String = "image/jpeg") {
        if (base64Image.isBlank()) return
        startLoading(imageLoadingMessages[0])

        viewModelScope.launch {
            val msgJob = launchLoadingMessages(imageLoadingMessages)
            val result = aiRepository.analyzeFoodFromImage(base64Image, mimeType)
            msgJob.cancel()
            handleResult(result) { analyzeFoodFromImage(base64Image, mimeType) }
        }
    }

    fun reset() {
        _uiState.value = NutritionUiState()
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorTitle = null, errorMessage = null, errorHint = null, cooldownSeconds = 0)
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private fun startLoading(firstMessage: String) {
        _uiState.update {
            it.copy(
                isLoading = true,
                result = null,
                errorTitle = null,
                errorMessage = null,
                errorHint = null,
                cooldownSeconds = 0,
                loadingMessage = firstMessage
            )
        }
    }

    private fun launchLoadingMessages(messages: List<String>): Job {
        var idx = 0
        return viewModelScope.launch {
            repeat(100) {
                delay(1800)
                idx = (idx + 1) % messages.size
                if (_uiState.value.isLoading) {
                    _uiState.update { s -> s.copy(loadingMessage = messages[idx]) }
                }
            }
        }
    }

    private fun handleResult(
        result: Result<NutritionInfo>,
        retryAction: () -> Unit
    ) {
        result
            .onSuccess { nutrition ->
                _uiState.update { it.copy(isLoading = false, result = nutrition) }
            }
            .onFailure { error ->
                val (title, message, hint) = mapError(error)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorTitle = title,
                        errorMessage = message,
                        errorHint = hint
                    )
                }
                // Jika rate limit, mulai countdown dan auto-retry
                if (error is AIError.RateLimited) {
                    startCooldownAndRetry(60, retryAction)
                }
            }
    }

    // ─── Countdown 60 detik lalu auto-retry ──────────────────────────────────
    private fun startCooldownAndRetry(seconds: Int, retryAction: () -> Unit) {
        viewModelScope.launch {
            for (remaining in seconds downTo 1) {
                _uiState.update { it.copy(cooldownSeconds = remaining) }
                delay(1000)
            }
            _uiState.update { it.copy(cooldownSeconds = 0) }
            retryAction()
        }
    }

    private fun mapError(error: Throwable): Triple<String, String, String> = when (error) {
        is AIError.Unauthorized -> Triple(
            "API Key Tidak Valid (401/403)",
            "API key salah atau tidak memiliki akses.",
            "Periksa kembali API key di aistudio.google.com"
        )
        is AIError.RateLimited -> Triple(
            "Rate Limit (429) — Auto-retry dalam 60 detik",
            "API key mencapai batas free tier.",
            "Menunggu otomatis lalu mencoba kembali..."
        )
        is AIError.ServerError -> Triple(
            "Server Error (5xx)",
            error.message ?: "Layanan Gemini sedang terganggu.",
            "Coba lagi dalam beberapa menit."
        )
        is AIError.NetworkError -> Triple(
            "Tidak Ada Koneksi",
            error.message ?: "Gagal menghubungi server.",
            "Pastikan perangkat terhubung ke internet."
        )
        is AIError.ParseError -> Triple(
            "Gagal Memproses Respons",
            error.message ?: "Format respons AI tidak valid.",
            "Coba gunakan nama makanan yang lebih spesifik."
        )
        is AIError.BadRequest -> Triple(
            "Permintaan Tidak Valid (400)",
            error.message ?: "Nama makanan tidak dapat diproses.",
            "Gunakan nama makanan yang lebih jelas."
        )
        else -> Triple(
            "Error Tak Terduga",
            error.message ?: "Terjadi kesalahan.",
            "Coba lagi."
        )
    }
}