package dev.myhappyplace.headlineduel.ui.viewmodel

import dev.myhappyplace.headlineduel.domain.model.ClassificationResult

data class HeadlineUiState(
    val headline: String = "",
    val userAnswer: String? = null,
    val modelResult: ClassificationResult? = null,
    val isLoading: Boolean = false,
    val correctClassification: String? = null
)