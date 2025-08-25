package dev.myhappyplace.headlineduel.data

import dev.myhappyplace.headlineduel.data.datasource.NewsClassifierRemoteDataSource
import dev.myhappyplace.headlineduel.domain.model.ClassificationResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsClassifierRepositoryImplTest {

    private val fakeRemoteDataSource = object : NewsClassifierRemoteDataSource {
        override suspend fun classifyHeadline(text: String): ClassificationResult {
            return ClassificationResult("Business", 0.88)
        }
    }

    @Test
    fun `repository delegates to remote data source`() = runTest {
        val repo = NewsClassifierRepositoryImpl(fakeRemoteDataSource)

        val result = repo.classifyHeadline("Markets crash after announcement")
        assertEquals("Business", result.label)
        assertEquals(0.88, result.score, 0.01)
    }
}