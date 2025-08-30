package dev.myhappyplace.headlineduel.data.repository

import dev.myhappyplace.headlineduel.data.datasource.HeadlineDataSource
import dev.myhappyplace.headlineduel.domain.model.Headline
import dev.myhappyplace.headlineduel.domain.repository.NewsHeadlineRepository

class NewsHeadlineRepositoryImpl(private val headlineDataSource: HeadlineDataSource) : NewsHeadlineRepository {
    override suspend fun getHeadline(excludedIndices: Set<Int>): Headline {
        return headlineDataSource.getHeadline(excludedIndices)
    }
}
