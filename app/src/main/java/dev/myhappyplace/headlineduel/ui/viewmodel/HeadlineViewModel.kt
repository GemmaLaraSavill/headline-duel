package dev.myhappyplace.headlineduel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.myhappyplace.headlineduel.domain.usecase.ClassifyHeadlineUseCase
import dev.myhappyplace.headlineduel.domain.usecase.GetHeadlineUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HeadlineViewModel(
    private val classifyHeadlineUseCase: ClassifyHeadlineUseCase,
    private val getHeadlineUseCase: GetHeadlineUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HeadlineUiState())
    val uiState: StateFlow<HeadlineUiState> = _uiState

    private val shownHeadlinesIndices = mutableSetOf<Int>()

    init {
        getHeadline()
    }

    fun getHeadline() {
        viewModelScope.launch {
            val headline = getHeadlineUseCase(shownHeadlinesIndices)
            shownHeadlinesIndices.add(headline.id)
            _uiState.value = _uiState.value.copy(headline = headline.text)
        }
    }

    fun onUserAnswer(answer: String) {
        val currentHeadline = _uiState.value.headline
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                userAnswer = answer,
                isLoading = true
            )
            val modelResult = classifyHeadlineUseCase(currentHeadline)
            _uiState.value = _uiState.value.copy(
                modelResult = modelResult,
                isLoading = false
            )
        }
    }

    fun nextHeadline() {
        _uiState.value = _uiState.value.copy(
            modelResult = null,
            userAnswer = null
        )
        getHeadline()
    }
}