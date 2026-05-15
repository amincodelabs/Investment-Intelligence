package amin.codelabs.realestateinvestmentintelligence.domain.repository

sealed interface RepositoryResult<out T> {
    data class Success<T>(
        val value: T,
    ) : RepositoryResult<T>

    data class Failure(
        val error: RepositoryError,
    ) : RepositoryResult<Nothing>
}

enum class RepositoryError {
    NotFound,
    AlreadyExists,
    Unknown,
}
