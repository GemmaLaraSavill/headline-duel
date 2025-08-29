package dev.myhappyplace.headlineduel.data.datasource

import dev.myhappyplace.headlineduel.domain.model.ClassificationResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class NewsClassifierRemoteDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String = "https://gemmalarasav-news-classifier-space.hf.space/gradio_api/call/predict"
) : NewsClassifierRemoteDataSource {

    @Serializable
    data class EventResponse(val event_id: String)

    @Serializable
    data class PredictionResponse(val value: List<String>)
    override suspend fun classifyHeadline(text: String): ClassificationResult {
        val eventResponse: EventResponse = client.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("data" to listOf(text)))
        }.body()

        val sseText = client.get("$baseUrl/${eventResponse.event_id}") {
            accept(ContentType.Text.EventStream)
        }.bodyAsText()

        val jsonLine = sseText.lineSequence()
            .firstOrNull { it.startsWith("data:") }
            ?.removePrefix("data:")
            ?.trim()

        val predictionList = if (jsonLine != null) {
            Json.decodeFromString<List<String>>(jsonLine)
        } else {
            listOf("Unknown (0.0)")
        }
        val raw = predictionList.firstOrNull() ?: "Unknown (0.0)"

        val parts = raw.split(" ")
        val label = parts.dropLast(1).joinToString(" ")
        val score = raw.substringAfter("(").substringBefore(")").toDoubleOrNull() ?: 0.0

        return ClassificationResult(label, score)
    }
}