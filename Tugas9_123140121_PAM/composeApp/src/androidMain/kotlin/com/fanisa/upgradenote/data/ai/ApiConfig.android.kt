package com.fanisa.upgradenote.data.ai

import com.fanisa.upgradenote.BuildConfig

actual object ApiConfig {
    actual val geminiApiKey: String
        get() {
            val fromBuildConfig = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
            return if (fromBuildConfig.isNotBlank() && fromBuildConfig != "your_gemini_api_key_here") {
                fromBuildConfig
            } else {
                // ⬇ Ganti dengan API key baru dari aistudio.google.com jika kena rate limit
                "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"
            }
        }
}