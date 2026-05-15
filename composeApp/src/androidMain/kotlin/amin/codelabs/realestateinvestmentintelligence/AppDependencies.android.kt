package amin.codelabs.realestateinvestmentintelligence

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@Composable
internal actual fun rememberPlatformAuthRepository(): AuthRepository {
    val appContext = LocalContext.current.applicationContext
    return remember(appContext) {
        EntryPointAccessors.fromApplication(
            appContext,
            AppRepositoryEntryPoint::class.java,
        ).authRepository()
    }
}

@Composable
internal actual fun rememberPlatformAreaRepository(): AreaRepository {
    val appContext = LocalContext.current.applicationContext
    return remember(appContext) {
        EntryPointAccessors.fromApplication(
            appContext,
            AppRepositoryEntryPoint::class.java,
        ).areaRepository()
    }
}

@Composable
internal actual fun rememberPlatformPropertyRepository(): PropertyRepository {
    val appContext = LocalContext.current.applicationContext
    return remember(appContext) {
        EntryPointAccessors.fromApplication(
            appContext,
            AppRepositoryEntryPoint::class.java,
        ).propertyRepository()
    }
}

@Composable
internal actual fun rememberPlatformWatchlistRepository(): WatchlistRepository {
    val appContext = LocalContext.current.applicationContext
    return remember(appContext) {
        EntryPointAccessors.fromApplication(
            appContext,
            AppRepositoryEntryPoint::class.java,
        ).watchlistRepository()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppRepositoryEntryPoint {
    fun authRepository(): AuthRepository
    fun areaRepository(): AreaRepository
    fun propertyRepository(): PropertyRepository
    fun watchlistRepository(): WatchlistRepository
}
