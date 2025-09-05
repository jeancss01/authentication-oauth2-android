package br.com.tcc.oauth2app.data.remote.model


data class ResponseLogin(
    val code: String,
    val expiresIn: Long
)

data class ResponseToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

data class ResponseAccount(
    val id: String,
    val name: String,
    val email: String,
    val country: String,
    val city: String,
    val state: String
)