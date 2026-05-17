package amin.codelabs.realestateinvestmentintelligence.calculator.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun InvestmentCalculatorRoute() {
    val viewModel = rememberInvestmentCalculatorViewModel()
    val effect = viewModel.effect

    InvestmentCalculatorScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
    )

    LaunchedEffect(effect) {
        if (effect != null) {
            viewModel.consumeEffect()
        }
    }
}
