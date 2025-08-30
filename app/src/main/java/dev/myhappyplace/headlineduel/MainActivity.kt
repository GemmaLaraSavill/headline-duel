package dev.myhappyplace.headlineduel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.myhappyplace.headlineduel.ui.screen.HeadlineScreen
import dev.myhappyplace.headlineduel.ui.screen.InformationScreen
import dev.myhappyplace.headlineduel.ui.theme.HeadlineDuelTheme
import dev.myhappyplace.headlineduel.ui.viewmodel.HeadlineViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val headlineViewModel: HeadlineViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeadlineDuelTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "headline") {
                    composable("headline") {
                        HeadlineScreen(
                            viewModel = headlineViewModel,
                            onNavigateToInfo = { navController.navigate("information") }
                        )
                    }
                    composable("information") {
                        InformationScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}