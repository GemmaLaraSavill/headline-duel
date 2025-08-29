package dev.myhappyplace.headlineduel.data.repository

import dev.myhappyplace.headlineduel.data.datasource.NewsClassifierRemoteDataSource
import dev.myhappyplace.headlineduel.domain.repository.NewsClassifierRepository

class NewsClassifierRepositoryImpl(
    private val remoteDataSource: NewsClassifierRemoteDataSource
) : NewsClassifierRepository {
    override suspend fun classifyHeadline(text: String) =
        remoteDataSource.classifyHeadline(text)
}