package amin.codelabs.realestateinvestmentintelligence.calculator.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidInvestmentCalculatorViewModel @Inject constructor() :
    ViewModel(),
    InvestmentCalculatorViewModel {
    private val delegate = DefaultInvestmentCalculatorViewModel()

    override val state: InvestmentCalculatorUiState
        get() = delegate.state

    override val effect: InvestmentCalculatorUiEffect?
        get() = delegate.effect

    override fun onEvent(event: InvestmentCalculatorUiEvent) {
        delegate.onEvent(event)
    }

    override fun consumeEffect() {
        delegate.consumeEffect()
    }
}

@Composable
internal actual fun rememberInvestmentCalculatorViewModel(): InvestmentCalculatorViewModel {
    return hiltViewModel<AndroidInvestmentCalculatorViewModel>()
}
