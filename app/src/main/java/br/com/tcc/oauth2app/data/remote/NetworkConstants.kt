package br.com.tcc.oauth2app.data.remote


object Headers {
    const val HEADER_TOKEN_KEY = "x-access-token"
    const val HEADER_AUTHORIZATION_KEY = "Authorization"
}

object NetworkURLs {
    const val BASE_URL_PRD = "http://192.168.15.80:5050/api/"
}

object OAuth2Constants {
    const val CLIENT_ID = "tcc_mba"
    const val GRANT_TYPE_AUTHORIZATION = "authorization_code"
    const val GRANT_TYPE_REFRESH = "refresh_token"
    const val REDIRECT_URI = "br.com.tcc.oauth2app://local"
    const val CODE_CHALLENGE_METHOD = "S256"
}