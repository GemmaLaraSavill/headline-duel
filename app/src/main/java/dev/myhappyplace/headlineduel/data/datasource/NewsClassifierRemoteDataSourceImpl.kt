package dev.myhappyplace.headlineduel.data.datasource

import dev.myhappyplace.headlineduel.domain.model.ClassificationResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class NewsClassifierRemoteDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String = "https://gemmalarasav-news-classifier-space.hf.space"
) : NewsClassifierRemoteDataSource {

    @Serializable
    data class PredictionResponse(val data: List<String>)

    override suspend fun classifyHeadline(text: String): ClassificationResult {
        val response: PredictionResponse = client.post("$baseUrl/run/predict") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("data" to listOf(text)))
        }.body()

        // Example response: ["Sci/Tech (0.97)"]
        val raw = response.data.firstOrNull() ?: "Unknown (0.0)"
        val parts = raw.split(" ")
        val label = parts.dropLast(1).joinToString(" ") // "Sci/Tech"
        val score = raw.substringAfter("(").substringBefore(")").toDoubleOrNull() ?: 0.0

        return ClassificationResult(label, score)
    }
}