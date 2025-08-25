package dev.myhappyplace.headlineduel.domain.repository

import dev.myhappyplace.headlineduel.domain.model.ClassificationResult

interface NewsClassifierRepository {
    suspend fun classifyHeadline(text: String): ClassificationResult
}