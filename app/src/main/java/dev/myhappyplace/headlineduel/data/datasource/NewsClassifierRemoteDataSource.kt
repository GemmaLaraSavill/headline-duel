package dev.myhappyplace.headlineduel.data.datasource

import dev.myhappyplace.headlineduel.domain.model.ClassificationResult

interface NewsClassifierRemoteDataSource {
    suspend fun classifyHeadline(text: String): ClassificationResult
}