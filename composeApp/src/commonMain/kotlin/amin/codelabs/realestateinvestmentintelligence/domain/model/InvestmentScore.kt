package amin.codelabs.realestateinvestmentintelligence.domain.model

data class InvestmentScore(
    val value: Int,
) {
    init {
        require(value in MIN_SCORE..MAX_SCORE) {
            "Investment score must be between $MIN_SCORE and $MAX_SCORE."
        }
    }

    companion object {
        const val MIN_SCORE = 0
        const val MAX_SCORE = 100

        fun from(value: Int): InvestmentScore = InvestmentScore(
            value.coerceIn(MIN_SCORE, MAX_SCORE),
        )
    }
}
