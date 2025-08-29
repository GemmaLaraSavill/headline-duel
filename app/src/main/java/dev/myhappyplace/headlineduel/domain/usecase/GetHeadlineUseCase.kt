package dev.myhappyplace.headlineduel.domain.usecase

import dev.myhappyplace.headlineduel.domain.model.Headline
import dev.myhappyplace.headlineduel.domain.repository.NewsHeadlineRepository

class GetHeadlineUseCase(private val newsHeadlineRepository: NewsHeadlineRepository) {
    suspend operator fun invoke(): Headline {
        return newsHeadlineRepository.getHeadline()
    }
}