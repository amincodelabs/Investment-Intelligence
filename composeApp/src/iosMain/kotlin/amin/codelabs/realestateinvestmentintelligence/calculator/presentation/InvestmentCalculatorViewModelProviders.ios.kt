package amin.codelabs.realestateinvestmentintelligence.calculator.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberInvestmentCalculatorViewModel(): InvestmentCalculatorViewModel {
    return remember { DefaultInvestmentCalculatorViewModel() }
}
