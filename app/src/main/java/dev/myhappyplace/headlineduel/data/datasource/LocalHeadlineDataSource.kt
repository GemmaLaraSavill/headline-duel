package dev.myhappyplace.headlineduel.data.datasource

import dev.myhappyplace.headlineduel.domain.model.Headline

class LocalHeadlineDataSource : HeadlineDataSource {
    override suspend fun getHeadline(): Headline {
        return Headline("NASA launches a new satellite to study climate change.")
    }
}
