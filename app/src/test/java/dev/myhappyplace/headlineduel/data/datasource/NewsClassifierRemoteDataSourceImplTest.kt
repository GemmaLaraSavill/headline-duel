package dev.myhappyplace.headlineduel.data.datasource

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType.Text.EventStream
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class NewsClassifierRemoteDataSourceImplTest {

    @Test
    fun `classifyHeadline successfully parses event_id and SSE data`() = runTest {
        val testEventId = "event123"
        val expectedLabel = "Sci/Tech"
        val expectedScore = 0.97

        val expectedPredictionJsonArrayString = """["$expectedLabel ($expectedScore)"]"""

        val mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                // Path for the first call (POST to get event_id)
                "/gradio_api/call/predict" -> {
                    println("MockEngine: Responding to /gradio_api/call/predict with event_id")
                    respond(
                        content = """{"event_id":"$testEventId"}""",
                        status = HttpStatusCode.OK,
                        headers = Headers.build {
                            append(
                                HttpHeaders.ContentType,
                                "application/json"
                            )
                        }
                    )
                }
                // Path for the second call (GET to get SSE data)
                "/gradio_api/call/predict/$testEventId" -> {
                    println("MockEngine: Responding to /gradio_api/call/predict/$testEventId with SSE")
                    val sseData = """
                        event: data
                        data: $expectedPredictionJsonArrayString

                        event: status
                        data: {"message": "Processing complete for $testEventId"}
                    """.trimIndent()
                    respond(
                        content = sseData,
                        status = HttpStatusCode.OK,
                        headers = Headers.build {
                            append(
                                HttpHeaders.ContentType,
                                EventStream.toString()
                            )
                        }
                    )
                }

                else -> {
                    println("MockEngine: Unexpected URL - ${request.url.encodedPath}")
                    error("MockEngine: Unexpected URL - ${request.url.encodedPath}")
                }
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }

        val dataSource = NewsClassifierRemoteDataSourceImpl(client = client)

        // Act
        val result = dataSource.classifyHeadline("A test headline")

        // Assert
        assertEquals(expectedLabel, result.label, "Label did not match")
        assertEquals(expectedScore, result.score, 0.001, "Score did not match")
    }



}