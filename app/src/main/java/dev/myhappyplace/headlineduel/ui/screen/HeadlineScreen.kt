package dev.myhappyplace.headlineduel.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.myhappyplace.headlineduel.ui.viewmodel.HeadlineViewModel

@Composable
fun HeadlineScreen(viewModel: HeadlineViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp),  verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = state.headline, style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        if (state.modelResult == null) {
            // Show options
            listOf("World", "Sports", "Business", "Sci/Tech").forEach { category ->
                Button(
                    onClick = { viewModel.onUserAnswer(category) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(category)
                }
            }
        } else {
            // Show results
            Text("Your answer: ${state.userAnswer}")
            Text("Model answer: ${state.modelResult!!.label} (${String.format("%.2f", state.modelResult!!.score)})")

            if (state.userAnswer == state.modelResult!!.label) {
                Text("✅ You got it right!", color = Color.Green)
            } else {
                Text("❌ Better luck next time!", color = Color.Red)
            }
        }
    }
}
