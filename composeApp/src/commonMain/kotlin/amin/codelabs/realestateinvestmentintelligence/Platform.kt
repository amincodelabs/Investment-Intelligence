package amin.codelabs.realestateinvestmentintelligence

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform