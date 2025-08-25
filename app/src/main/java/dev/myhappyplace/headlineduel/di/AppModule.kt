package dev.myhappyplace.headlineduel.di

import dev.myhappyplace.headlineduel.data.NewsClassifierRepositoryImpl
import dev.myhappyplace.headlineduel.domain.repository.NewsClassifierRepository
import dev.myhappyplace.headlineduel.domain.usecase.ClassifyHeadlineUseCase
import org.koin.dsl.module

val appModule = module {
    single<NewsClassifierRepository> { NewsClassifierRepositoryImpl(get()) }
    factory { ClassifyHeadlineUseCase(get()) }
}