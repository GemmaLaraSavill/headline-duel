package dev.myhappyplace.headlineduel.domain.repository

import dev.myhappyplace.headlineduel.domain.model.Headline

interface NewsHeadlineRepository {
    suspend fun getHeadline(excludedIndices: Set<Int>): Headline
}