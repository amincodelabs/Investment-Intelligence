package amin.codelabs.realestateinvestmentintelligence.dashboard.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetDashboardOverviewUseCase
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidDashboardViewModel @Inject constructor(
    getDashboardOverviewUseCase: GetDashboardOverviewUseCase,
) : ViewModel(), DashboardViewModel {
    private val delegate = DefaultDashboardViewModel(
        getDashboardOverviewUseCase = getDashboardOverviewUseCase,
    )

    override val state: DashboardUiState
        get() = delegate.state

    override val effect: DashboardUiEffect?
        get() = delegate.effect

    override fun onEvent(event: DashboardUiEvent) {
        delegate.onEvent(event)
    }

    override fun consumeEffect() {
        delegate.consumeEffect()
    }
}

@Composable
internal actual fun rememberDashboardViewModel(
    areaRepository: AreaRepository,
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
): DashboardViewModel = hiltViewModel<AndroidDashboardViewModel>()
