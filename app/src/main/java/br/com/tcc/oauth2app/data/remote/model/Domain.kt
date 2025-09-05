
data class LoginDomain (
    val code: String,
    val expiresIn: Long
)

data class TokenDomain (
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

data class AccountDomain(
    val id: String,
    val name: String,
    val email: String,
    val country: String,
    val city: String,
    val state: String
)