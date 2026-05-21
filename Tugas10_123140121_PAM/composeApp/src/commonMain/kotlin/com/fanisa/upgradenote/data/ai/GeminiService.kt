package com.fanisa.upgradenote.data.ai

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

// ─── Response model Groq ──────────────────────────────────────────────────────

@Serializable
data class GroqResponse(
    val choices: List<GroqChoice> = emptyList()
)

@Serializable
data class GroqChoice(
    val message: GroqMessage
)

@Serializable
data class GroqMessage(
    val role: String = "",
    val content: String = ""
)

// ─── GeminiService — backend Groq ────────────────────────────────────────────

class GeminiService {

    private val baseUrl = "https://api.groq.com/openai/v1"
    private val model   = "llama-3.3-70b-versatile"

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    val httpClient: HttpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(jsonParser)
            }
        }
    }

    private val systemPrompt = """
You are a professional nutritionist. Always respond in Bahasa Indonesia.
Detect food from any language. Respond ONLY with valid JSON. No markdown, no backticks.
    """.trimIndent()

    // ─── Prompt teks ──────────────────────────────────────────────────────────
    fun buildTextPrompt(foodName: String, amount: String, unit: String): String = """
Analisis kandungan gizi: $foodName ($amount $unit)

Balas HANYA dengan JSON (semua nilai numerik harus angka, bukan string):
{
  "name": "nama makanan dalam bahasa Indonesia",
  "emoji": "satu emoji",
  "portion": "$amount $unit",
  "calories": 300,
  "protein": 10.5,
  "carbs": 45.0,
  "fat": 8.2,
  "fiber": 2.1,
  "sugar": 5.0,
  "sodium": 400,
  "cholesterol": 30,
  "healthScore": 7,
  "healthCategory": "Cukup Sehat",
  "scoreExplanation": "penjelasan singkat",
  "tips": ["tip1", "tip2", "tip3"]
}
    """.trimIndent()

    // ─── Prompt gambar ────────────────────────────────────────────────────────
    fun buildImagePrompt(): String = """
Identifikasi makanan dari foto dan analisis gizinya (1 porsi standar).

Balas HANYA dengan JSON (semua nilai numerik harus angka, bukan string):
{
  "name": "nama makanan",
  "emoji": "satu emoji",
  "portion": "1 porsi",
  "calories": 300,
  "protein": 10.5,
  "carbs": 45.0,
  "fat": 8.2,
  "fiber": 2.1,
  "sugar": 5.0,
  "sodium": 400,
  "cholesterol": 30,
  "healthScore": 7,
  "healthCategory": "Cukup Sehat",
  "scoreExplanation": "penjelasan singkat",
  "tips": ["tip1", "tip2", "tip3"]
}
    """.trimIndent()

    // ─── Panggil API teks ─────────────────────────────────────────────────────
    suspend fun callTextApi(prompt: String): Result<String> {
        return try {
            val body = buildRequestJson(model, systemPrompt, prompt)
            callGroqApi(body)
        } catch (e: Exception) {
            mapException(e)
        }
    }

    // ─── Panggil API gambar ───────────────────────────────────────────────────
    suspend fun callImageApi(
        base64Image: String,
        mimeType: String = "image/jpeg"
    ): Result<String> {
        return try {
            val visionModel = "meta-llama/llama-4-scout-17b-16e-instruct"
            val contentArray = buildJsonArray {
                addJsonObject {
                    put("type", "text")
                    put("text", buildImagePrompt())
                }
                addJsonObject {
                    put("type", "image_url")
                    putJsonObject("image_url") {
                        put("url", "data:$mimeType;base64,$base64Image")
                    }
                }
            }
            val body = buildJsonObject {
                put("model", visionModel)
                put("max_tokens", 800)
                put("temperature", 0.3)
                putJsonArray("messages") {
                    addJsonObject {
                        put("role", "system")
                        put("content", systemPrompt)
                    }
                    addJsonObject {
                        put("role", "user")
                        put("content", contentArray)
                    }
                }
            }.toString()
            callGroqApi(body)
        } catch (e: Exception) {
            mapException(e)
        }
    }

    // ─── Build request JSON untuk teks ───────────────────────────────────────
    private fun buildRequestJson(
        modelName: String,
        system: String,
        userPrompt: String
    ): String {
        return buildJsonObject {
            put("model", modelName)
            put("max_tokens", 800)
            put("temperature", 0.3)
            putJsonArray("messages") {
                addJsonObject {
                    put("role", "system")
                    put("content", system)
                }
                addJsonObject {
                    put("role", "user")
                    put("content", userPrompt)
                }
            }
        }.toString()
    }

    // ─── HTTP call ke Groq ────────────────────────────────────────────────────
    private suspend fun callGroqApi(jsonBody: String): Result<String> {
        val response: HttpResponse = httpClient.post("$baseUrl/chat/completions") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${ApiConfig.geminiApiKey}")
            setBody(jsonBody)
        }

        return when (response.status.value) {
            200 -> {
                try {
                    // Parse response body sebagai GroqResponse
                    val groqResp: GroqResponse = response.body()
                    val content = groqResp.choices.firstOrNull()?.message?.content
                        ?: return Result.failure(AIError.ParseError("Respons AI kosong."))
                    Result.success(content)
                } catch (e: Exception) {
                    // Fallback: ambil dari raw text jika body() gagal
                    val raw = response.bodyAsText()
                    extractContentFromRaw(raw)
                }
            }
            401, 403 -> Result.failure(AIError.Unauthorized())
            429      -> Result.failure(AIError.RateLimited())
            400      -> {
                val err = try { response.bodyAsText() } catch (e: Exception) { "" }
                Result.failure(AIError.BadRequest("400: ${err.take(100)}"))
            }
            in 500..599 -> Result.failure(AIError.ServerError())
            else -> Result.failure(AIError.ServerError("HTTP ${response.status.value}"))
        }
    }

    // ─── Fallback: ekstrak content dari raw JSON string ───────────────────────
    private fun extractContentFromRaw(raw: String): Result<String> {
        return try {
            val jsonObj = jsonParser.parseToJsonElement(raw).jsonObject
            val choices = jsonObj["choices"]?.jsonArray
            val content = choices
                ?.firstOrNull()?.jsonObject
                ?.get("message")?.jsonObject
                ?.get("content")?.jsonPrimitive?.content
                ?: return Result.failure(AIError.ParseError("Tidak dapat membaca respons."))
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(AIError.ParseError("Parse raw gagal: ${e.message?.take(80)}"))
        }
    }

    // ─── Parse JSON nutrisi dari teks respons AI ─────────────────────────────
    fun parseNutritionJson(rawText: String): Result<NutritionInfo> {
        // Cari blok JSON pertama dalam teks
        val jsonStart = rawText.indexOf('{')
        val jsonEnd   = rawText.lastIndexOf('}')
        if (jsonStart == -1 || jsonEnd == -1 || jsonEnd <= jsonStart) {
            return Result.failure(AIError.ParseError("Tidak ada JSON dalam respons."))
        }
        val cleanJson = rawText.substring(jsonStart, jsonEnd + 1)
        return try {
            val info = jsonParser.decodeFromString<NutritionInfo>(cleanJson)
            Result.success(info)
        } catch (e: Exception) {
            Result.failure(AIError.ParseError("Parse gagal: ${e.message?.take(80)}"))
        }
    }

    // ─── Map exception ke AIError ─────────────────────────────────────────────
    private fun <T> mapException(e: Exception): Result<T> {
        val msg = e.message ?: ""
        return when {
            msg.contains("UnknownHostException", ignoreCase = true) ||
                    msg.contains("Unable to resolve", ignoreCase = true)    ||
                    msg.contains("SocketException", ignoreCase = true)      ||
                    msg.contains("timeout", ignoreCase = true) ->
                Result.failure(AIError.NetworkError("Periksa koneksi internet Anda."))
            msg.contains("401") || msg.contains("403") ->
                Result.failure(AIError.Unauthorized())
            msg.contains("429") ->
                Result.failure(AIError.RateLimited())
            else ->
                Result.failure(AIError.NetworkError(msg.take(120)))
        }
    }
}