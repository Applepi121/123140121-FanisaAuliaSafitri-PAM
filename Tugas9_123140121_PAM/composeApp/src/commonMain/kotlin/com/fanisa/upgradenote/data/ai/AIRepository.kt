package com.fanisa.upgradenote.data.ai

import kotlinx.coroutines.delay

class AIRepository(private val geminiService: GeminiService) {

    // ─── Analisis dari nama teks (semua bahasa) ───────────────────────────────
    suspend fun analyzeFood(
        foodName: String,
        amount: String,
        unit: String,
        maxRetries: Int = 3
    ): Result<NutritionInfo> {
        val prompt = geminiService.buildTextPrompt(foodName, amount, unit)
        return retryCall(maxRetries) {
            geminiService.callTextApi(prompt)
        }
    }

    // ─── Analisis dari foto (base64 image) ───────────────────────────────────
    suspend fun analyzeFoodFromImage(
        base64Image: String,
        mimeType: String = "image/jpeg",
        maxRetries: Int = 2
    ): Result<NutritionInfo> {
        return retryCall(maxRetries) {
            geminiService.callImageApi(base64Image, mimeType)
        }
    }

    // ─── Retry dengan exponential backoff ────────────────────────────────────
    private suspend fun retryCall(
        maxRetries: Int,
        block: suspend () -> Result<String>
    ): Result<NutritionInfo> {
        var lastError: Throwable = AIError.NetworkError()

        repeat(maxRetries) { attempt ->
            val apiResult = block()

            if (apiResult.isFailure) {
                lastError = apiResult.exceptionOrNull() ?: lastError
                // Jangan retry untuk error permanen
                if (lastError is AIError.Unauthorized || lastError is AIError.BadRequest) {
                    return Result.failure(lastError)
                }
                // Exponential backoff: 1s → 2s → 4s
                if (attempt < maxRetries - 1) {
                    delay((1000L * (1 shl attempt)).coerceAtMost(8000L))
                }
                return@repeat
            }

            // Parse JSON dari respons sukses
            val rawText = apiResult.getOrNull() ?: return@repeat
            val parseResult = geminiService.parseNutritionJson(rawText)
            if (parseResult.isSuccess) return parseResult
            lastError = parseResult.exceptionOrNull() ?: lastError
        }

        return Result.failure(lastError)
    }
}