package dev.myhappyplace.headlineduel.di

import dev.myhappyplace.headlineduel.data.NewsClassifierRepositoryImpl
import dev.myhappyplace.headlineduel.data.datasource.HeadlineDataSource
import dev.myhappyplace.headlineduel.data.datasource.LocalHeadlineDataSource
import dev.myhappyplace.headlineduel.data.datasource.NewsClassifierRemoteDataSource
import dev.myhappyplace.headlineduel.data.datasource.NewsClassifierRemoteDataSourceImpl
import dev.myhappyplace.headlineduel.data.network.KtorClientProvider
import dev.myhappyplace.headlineduel.data.repository.NewsHeadlineRepositoryImpl
import dev.myhappyplace.headlineduel.domain.repository.NewsClassifierRepository
import dev.myhappyplace.headlineduel.domain.repository.NewsHeadlineRepository
import dev.myhappyplace.headlineduel.domain.usecase.ClassifyHeadlineUseCase
import dev.myhappyplace.headlineduel.domain.usecase.GetHeadlineUseCase
import dev.myhappyplace.headlineduel.ui.viewmodel.HeadlineViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<HttpClient> { KtorClientProvider.client }
    single<NewsClassifierRemoteDataSource> { NewsClassifierRemoteDataSourceImpl(get()) }
    single<NewsClassifierRepository> { NewsClassifierRepositoryImpl(get()) }
    factory { ClassifyHeadlineUseCase(get()) }
    factory { GetHeadlineUseCase(get()) }
    viewModel {
        HeadlineViewModel(
            classifyHeadlineUseCase = get<ClassifyHeadlineUseCase>(),
            getHeadlineUseCase = get<GetHeadlineUseCase>()
        )
    }

    single<HeadlineDataSource> { LocalHeadlineDataSource() }
    single<NewsHeadlineRepository> { NewsHeadlineRepositoryImpl(get()) }
}