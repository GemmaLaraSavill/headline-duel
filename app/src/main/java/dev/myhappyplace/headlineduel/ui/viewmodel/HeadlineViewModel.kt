package dev.myhappyplace.headlineduel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.myhappyplace.headlineduel.domain.usecase.ClassifyHeadlineUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HeadlineViewModel(
    private val classifyHeadlineUseCase: ClassifyHeadlineUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HeadlineUiState())
    val uiState: StateFlow<HeadlineUiState> = _uiState

    fun setHeadline(headline: String) {
        _uiState.value = HeadlineUiState(headline = headline)
    }

    fun onUserAnswer(answer: String) {
        val currentHeadline = _uiState.value.headline
        viewModelScope.launch {
            val modelResult = classifyHeadlineUseCase(currentHeadline)
            _uiState.value = _uiState.value.copy(
                userAnswer = answer,
                modelResult = modelResult
            )
        }
    }
}