package amin.codelabs.realestateinvestmentintelligence.data.auth

import amin.codelabs.realestateinvestmentintelligence.domain.model.Email
import amin.codelabs.realestateinvestmentintelligence.domain.model.User
import amin.codelabs.realestateinvestmentintelligence.domain.model.UserId
import kotlin.math.absoluteValue

class DemoAuthDataSource {
    private val usersByEmail = mutableMapOf<String, DemoUserRecord>()
    private var nextUserNumber = 1

    fun findUserByEmail(email: Email): DemoUserRecord? = usersByEmail[email.normalized]

    fun saveUser(
        fullName: String,
        email: Email,
        password: String,
    ): DemoUserRecord {
        val user = User(
            id = UserId("demo-user-${nextUserNumber++}"),
            fullName = fullName.trim(),
            email = email,
        )
        val record = DemoUserRecord(
            user = user,
            passwordFingerprint = DemoPasswordFingerprint.from(password),
        )

        usersByEmail[email.normalized] = record
        return record
    }
}

data class DemoUserRecord(
    val user: User,
    val passwordFingerprint: String,
) {
    fun matchesPassword(password: String): Boolean {
        return passwordFingerprint == DemoPasswordFingerprint.from(password)
    }
}

private object DemoPasswordFingerprint {
    fun from(password: String): String {
        // Demo-only verifier. Future production auth must be backend-managed and never use this.
        return "demo-${password.hashCode().absoluteValue}"
    }
}
