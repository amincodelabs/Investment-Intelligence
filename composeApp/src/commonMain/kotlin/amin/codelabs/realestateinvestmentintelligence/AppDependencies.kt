package amin.codelabs.realestateinvestmentintelligence

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable

@Composable
internal expect fun rememberPlatformAuthRepository(): AuthRepository

@Composable
internal expect fun rememberPlatformAreaRepository(): AreaRepository

@Composable
internal expect fun rememberPlatformPropertyRepository(): PropertyRepository

@Composable
internal expect fun rememberPlatformWatchlistRepository(): WatchlistRepository
