package com.fanisa.upgradenote.data.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ─── Gemini Request Models ────────────────────────────────────────────────────

@Serializable
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig? = null
)

@Serializable
data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String = "user"
)

@Serializable
data class GeminiPart(
    val text: String? = null,
    @SerialName("inline_data") val inlineData: GeminiInlineData? = null
)

@Serializable
data class GeminiInlineData(
    @SerialName("mime_type") val mimeType: String,
    val data: String  // base64 encoded image
)

@Serializable
data class GeminiGenerationConfig(
    val temperature: Double = 0.3,
    @SerialName("maxOutputTokens") val maxOutputTokens: Int = 1000,
    val topP: Double = 0.95
)

// ─── Gemini Response Models ───────────────────────────────────────────────────

@Serializable
data class GeminiResponse(
    val candidates: List<GeminiCandidate> = emptyList()
)

@Serializable
data class GeminiCandidate(
    val content: GeminiContent,
    val finishReason: String? = null
)

// ─── Nutrition Result Model ───────────────────────────────────────────────────

@Serializable
data class NutritionInfo(
    val name: String,
    val emoji: String = "🍽️",
    val portion: String,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val fiber: Double,
    val sugar: Double,
    val sodium: Int,
    val cholesterol: Int,
    val healthScore: Int,
    val healthCategory: String,
    val scoreExplanation: String,
    val tips: List<String>
)

// ─── AI Error Sealed Class ────────────────────────────────────────────────────

sealed class AIError : Exception() {
    data class RateLimited(val retryAfterSeconds: Int = 60) : AIError() {
        override val message = "Terlalu banyak permintaan. Coba lagi dalam $retryAfterSeconds detik."
    }
    data class Unauthorized(
        override val message: String = "API key tidak valid atau kedaluwarsa."
    ) : AIError()
    data class ServerError(
        override val message: String = "Server Gemini sedang bermasalah."
    ) : AIError()
    data class NetworkError(
        override val message: String = "Tidak ada koneksi internet."
    ) : AIError()
    data class ParseError(
        override val message: String = "Gagal memproses respons AI."
    ) : AIError()
    data class BadRequest(
        override val message: String = "Permintaan tidak valid."
    ) : AIError()
}