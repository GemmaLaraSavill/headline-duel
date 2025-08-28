package dev.myhappyplace.headlineduel.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.myhappyplace.headlineduel.ui.viewmodel.HeadlineUiState

@Composable
fun HeadlineScreen(
    uiState: HeadlineUiState,
    onAnswerSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = uiState.headline,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        val categories = listOf("World", "Sports", "Business", "Sci/Tech")
        categories.forEach { category ->
            Button(
                onClick = { onAnswerSelected(category) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(category)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HeadlineScreenPreview() {
    MaterialTheme {
        HeadlineScreen(
            uiState = HeadlineUiState(headline = "This is a sample headline for preview!"),
            onAnswerSelected = { }
        )
    }
}