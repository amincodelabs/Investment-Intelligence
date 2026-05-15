package amin.codelabs.realestateinvestmentintelligence.auth.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidLoginViewModel @Inject constructor(
    authRepository: AuthRepository,
) : ViewModel(), LoginViewModel {
    private val delegate = DefaultLoginViewModel(authRepository = authRepository)

    override val state: LoginUiState
        get() = delegate.state

    override val effect: LoginUiEffect?
        get() = delegate.effect

    override fun onEvent(event: LoginUiEvent) {
        delegate.onEvent(event)
    }

    override fun consumeEffect() {
        delegate.consumeEffect()
    }
}

@HiltViewModel
class AndroidRegisterViewModel @Inject constructor(
    authRepository: AuthRepository,
) : ViewModel(), RegisterViewModel {
    private val delegate = DefaultRegisterViewModel(authRepository = authRepository)

    override val state: RegisterUiState
        get() = delegate.state

    override val effect: RegisterUiEffect?
        get() = delegate.effect

    override fun onEvent(event: RegisterUiEvent) {
        delegate.onEvent(event)
    }

    override fun consumeEffect() {
        delegate.consumeEffect()
    }
}

@Composable
internal actual fun rememberLoginViewModel(
    authRepository: AuthRepository,
): LoginViewModel = hiltViewModel<AndroidLoginViewModel>()

@Composable
internal actual fun rememberRegisterViewModel(
    authRepository: AuthRepository,
): RegisterViewModel = hiltViewModel<AndroidRegisterViewModel>()
