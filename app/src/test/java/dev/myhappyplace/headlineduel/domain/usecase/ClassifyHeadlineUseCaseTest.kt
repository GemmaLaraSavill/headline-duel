package dev.myhappyplace.headlineduel.domain.usecase

import dev.myhappyplace.headlineduel.domain.model.ClassificationResult
import dev.myhappyplace.headlineduel.domain.repository.NewsClassifierRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ClassifyHeadlineUseCaseTest {

    @MockK
    private lateinit var fakeRepo: NewsClassifierRepository

    private lateinit var useCase: ClassifyHeadlineUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = ClassifyHeadlineUseCase(fakeRepo)
    }

    @Test
    fun `When use case is called Then it returns correct classification`() = runTest {

        val headlineToTest = "NASA launches rocket"
        val expectedClassification = ClassificationResult("Sci/Tech", 0.97)
        coEvery { fakeRepo.classifyHeadline(headlineToTest) } returns expectedClassification


        val result = useCase(headlineToTest)

        assertEquals("Sci/Tech", result.label)
        assertEquals(0.97, result.score, 0.001) 
    }
}
