package dev.myhappyplace.headlineduel.domain.usecase

import dev.myhappyplace.headlineduel.di.appModule
import dev.myhappyplace.headlineduel.domain.model.ClassificationResult
import dev.myhappyplace.headlineduel.domain.repository.NewsClassifierRepository
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

class ClassifyHeadlineUseCaseTest : KoinTest {

    private val useCase: ClassifyHeadlineUseCase by inject()
    private lateinit var fakeRepo: NewsClassifierRepository

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @Before
    fun setUp() {
        startKoin {
            modules(appModule)
        }
        fakeRepo = declareMock<NewsClassifierRepository>()
    }

    @After
    fun tearDown() {
        stopKoin()
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
