package dev.myhappyplace.headlineduel.ui.viewmodel

import dev.myhappyplace.headlineduel.domain.model.ClassificationResult

data class HeadlineUiState(
    val headline: String = "NASA launches a new satellite to study climate change.",
    val userAnswer: String? = null,
    val modelResult: ClassificationResult? = null,
    val isLoading: Boolean = false
)