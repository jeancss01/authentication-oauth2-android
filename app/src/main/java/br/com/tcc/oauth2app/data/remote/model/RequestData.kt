package br.com.tcc.oauth2app.data.remote.model

import br.com.tcc.oauth2app.data.remote.OAuth2Constants

data class RequestDataLogin(
    val email: String,
    val password: String,
    val codeChallenge: String,
    val codeChallengeMethod: String = OAuth2Constants.CODE_CHALLENGE_METHOD,
    val clientId: String = OAuth2Constants.CLIENT_ID,
    val redirectUri: String = OAuth2Constants.REDIRECT_URI
)

class RequestDataToken(
    val code: String,
    val codeVerifier: String,
    val clientId: String = OAuth2Constants.CLIENT_ID,
    val grantType: String = OAuth2Constants.GRANT_TYPE_AUTHORIZATION
)

class RequestDataRefreshToken(
    val refreshToken: String,
    val grantType: String = OAuth2Constants.GRANT_TYPE_REFRESH
)