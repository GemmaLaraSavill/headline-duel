package dev.myhappyplace.headlineduel.data.datasource

import dev.myhappyplace.headlineduel.domain.model.Headline

interface HeadlineDataSource {
    suspend fun getHeadline(): Headline
}
