package dev.myhappyplace.headlineduel.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.myhappyplace.headlineduel.R
import dev.myhappyplace.headlineduel.domain.model.ClassificationResult
import dev.myhappyplace.headlineduel.ui.viewmodel.HeadlineViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlineScreen(viewModel: HeadlineViewModel) {
    val state by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val locale = configuration.locales[0]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                LoadingState()
            } else {
                Text(text = state.headline, style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                if (state.modelResult == null) {
                    val categories = listOf(
                        R.string.world,
                        R.string.sports,
                        R.string.business,
                        R.string.sci_tech
                    )
                    QuestionState(categories = categories, onAnswer = viewModel::onUserAnswer)
                } else {
                    state.userAnswer?.let { userAnswer ->
                        state.modelResult?.let { modelResult ->
                            AnswerState(
                                userAnswer = userAnswer,
                                modelResult = modelResult,
                                onNext = viewModel::nextHeadline,
                                locale = locale
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingState() {
    CircularProgressIndicator()
}

@Composable
fun QuestionState(categories: List<Int>, onAnswer: (String) -> Unit) {
    categories.forEach { resId ->
        val category = stringResource(id = resId)
        Button(
            onClick = { onAnswer(category) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(stringResource(id = resId))
        }
    }
}

@Composable
fun AnswerState(
    userAnswer: String,
    modelResult: ClassificationResult,
    onNext: () -> Unit,
    locale: Locale
) {
    val score = String.format(locale, "%.2f", modelResult.score)
    Text(stringResource(id = R.string.your_answer, userAnswer))
    Text(stringResource(id = R.string.model_answer, modelResult.label, score))

    if (userAnswer == modelResult.label) {
        Text(stringResource(id = R.string.correct_answer), color = Color.Green)
    } else {
        Text(stringResource(id = R.string.wrong_answer), color = Color.Red)
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onNext,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(stringResource(id = R.string.next))
    }
}
