package dev.myhappyplace.headlineduel.data.datasource

import dev.myhappyplace.headlineduel.domain.model.ClassificationResult
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
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
    fun `parses response correctly`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = """{"data":["Sci/Tech (0.97)"]}""",
                status = HttpStatusCode.OK,
                headers = Headers.build {
                    append(HttpHeaders.ContentType, "application/json")
                }
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val dataSource = NewsClassifierRemoteDataSourceImpl(client = client)

        val result: ClassificationResult = dataSource.classifyHeadline("Test headline")
        assertEquals("Sci/Tech", result.label)
        assertEquals(0.97, result.score, 0.001)
    }
}