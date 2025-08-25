package dev.myhappyplace.headlineduel.domain.usecase

import dev.myhappyplace.headlineduel.domain.model.ClassificationResult
import dev.myhappyplace.headlineduel.domain.repository.NewsClassifierRepository

class ClassifyHeadlineUseCase(
    private val repository: NewsClassifierRepository
) {
    suspend operator fun invoke(text: String): ClassificationResult {
        return repository.classifyHeadline(text)
    }
}