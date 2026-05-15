package amin.codelabs.realestateinvestmentintelligence.di

import amin.codelabs.realestateinvestmentintelligence.data.auth.AuthEventLogger
import amin.codelabs.realestateinvestmentintelligence.data.auth.DemoAuthDataSource
import amin.codelabs.realestateinvestmentintelligence.data.auth.NoOpAuthEventLogger
import amin.codelabs.realestateinvestmentintelligence.data.repository.DemoAuthRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideDemoAuthDataSource(): DemoAuthDataSource = DemoAuthDataSource()

    @Provides
    fun provideAuthEventLogger(): AuthEventLogger = NoOpAuthEventLogger

    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataSource: DemoAuthDataSource,
        logger: AuthEventLogger,
    ): AuthRepository = DemoAuthRepository(
        authDataSource = authDataSource,
        logger = logger,
    )
}
