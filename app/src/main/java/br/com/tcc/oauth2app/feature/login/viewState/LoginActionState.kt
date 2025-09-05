package br.com.tcc.oauth2app.feature.login.viewState

sealed interface LoginActionState {
    data class ValidCredentials(val status: Boolean) : LoginActionState

    data class InvalidCredentialsError(val message: String) : LoginActionState
}