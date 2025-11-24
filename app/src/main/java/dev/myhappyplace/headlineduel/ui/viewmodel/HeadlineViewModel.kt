package dev.myhappyplace.headlineduel.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.myhappyplace.headlineduel.domain.usecase.ClassifyHeadlineUseCase
import dev.myhappyplace.headlineduel.domain.usecase.GetHeadlineUseCase
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
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
            _uiState.value = _uiState.value.copy(
                headline = headline.text,
                correctClassification = headline.correctClassification
            )
        }
    }

    fun onUserAnswer(answer: String) {
        val currentHeadline = _uiState.value.headline
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                userAnswer = answer,
                isLoading = true,
                error = null
            )
            try {
                val modelResult = classifyHeadlineUseCase(currentHeadline)
                _uiState.value = _uiState.value.copy(
                    modelResult = modelResult,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = getFriendlyError(e)
                )
            }
        }
    }

    private fun getFriendlyError(e: Exception): HeadlineError {
        return when (e) {
            is HttpRequestTimeoutException,
            is ConnectTimeoutException,
            is SocketTimeoutException -> HeadlineError.SERVER_WARMING_UP
            else -> HeadlineError.GENERIC_CONNECTION_ERROR
        }
    }

    fun retry() {
        val answer = _uiState.value.userAnswer
        if (answer != null) {
            onUserAnswer(answer)
        }
    }

    fun nextHeadline() {
        _uiState.value = _uiState.value.copy(
            modelResult = null,
            userAnswer = null,
            correctClassification = null,
            error = null
        )
        getHeadline()
    }
}