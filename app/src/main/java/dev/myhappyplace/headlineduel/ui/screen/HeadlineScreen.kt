package dev.myhappyplace.headlineduel.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.myhappyplace.headlineduel.R
import dev.myhappyplace.headlineduel.domain.model.ClassificationResult
import dev.myhappyplace.headlineduel.ui.theme.CorrectAnswerBackgroundDark
import dev.myhappyplace.headlineduel.ui.theme.CorrectAnswerBackgroundLight
import dev.myhappyplace.headlineduel.ui.theme.CorrectAnswerTextDark
import dev.myhappyplace.headlineduel.ui.theme.CorrectAnswerTextLight
import dev.myhappyplace.headlineduel.ui.theme.WrongAnswerBackgroundDark
import dev.myhappyplace.headlineduel.ui.theme.WrongAnswerBackgroundLight
import dev.myhappyplace.headlineduel.ui.theme.WrongAnswerTextDark
import dev.myhappyplace.headlineduel.ui.theme.WrongAnswerTextLight
import dev.myhappyplace.headlineduel.ui.viewmodel.HeadlineViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HeadlineScreen(viewModel: HeadlineViewModel, onNavigateToInfo: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val locale = configuration.locales[0]

    val uiAnimationState = when {
        state.isLoading -> HeadlineScreenAnimationState.Loading
        state.modelResult == null -> HeadlineScreenAnimationState.Question
        else -> HeadlineScreenAnimationState.Answer
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    IconButton(onClick = { onNavigateToInfo() }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.information),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.headline_prompt),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                ),
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(all = 16.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = state.headline,
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = uiAnimationState,
                label = "UiStateAnimation",
                transitionSpec = {
                    if (targetState == HeadlineScreenAnimationState.Answer && initialState == HeadlineScreenAnimationState.Loading) {
                        fadeIn() togetherWith fadeOut()
                    } else {
                        fadeIn() togetherWith fadeOut()
                    }
                }
            ) { targetUiState ->
                when (targetUiState) {
                    HeadlineScreenAnimationState.Loading -> LoadingState()
                    HeadlineScreenAnimationState.Question -> {
                        val categories = listOf(
                            R.string.world,
                            R.string.sports,
                            R.string.business,
                            R.string.sci_tech
                        )
                        QuestionState(
                            categories = categories,
                            onAnswer = viewModel::onUserAnswer
                        )
                    }

                    HeadlineScreenAnimationState.Answer -> {
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
}

@Composable
fun LoadingState() {
    CircularProgressIndicator()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionState(categories: List<Int>, onAnswer: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        categories.forEach { resId ->
            val category = stringResource(id = resId)
            OutlinedButton(
                onClick = { onAnswer(category) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                val icon = when (resId) {
                    R.string.world -> Icons.Default.Public
                    R.string.sports -> Icons.Default.Sports
                    R.string.business -> Icons.Default.Business
                    else -> Icons.Default.Science
                }
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(id = resId),
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(stringResource(id = resId), fontSize = 22.sp)
            }
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
    val isCorrect = userAnswer == modelResult.label
    val cardBackgroundColor = if (isCorrect) {
        if (isSystemInDarkTheme()) CorrectAnswerBackgroundDark else CorrectAnswerBackgroundLight
    } else {
        if (isSystemInDarkTheme()) WrongAnswerBackgroundDark else WrongAnswerBackgroundLight
    }
    val cardColors = CardDefaults.cardColors(containerColor = cardBackgroundColor)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = cardColors
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.your_answer, userAnswer),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                val score = String.format(locale, "%.2f", modelResult.score)
                Text(
                    text = stringResource(id = R.string.model_answer, modelResult.label, score),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isCorrect) {
            Text(
                text = stringResource(id = R.string.correct_answer),
                color = if (isSystemInDarkTheme()) CorrectAnswerTextDark else CorrectAnswerTextLight,
                style = MaterialTheme.typography.headlineMedium
            )
        } else {
            Text(
                text = stringResource(id = R.string.wrong_answer),
                color = if (isSystemInDarkTheme()) WrongAnswerTextDark else WrongAnswerTextLight,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(stringResource(id = R.string.next), fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}
