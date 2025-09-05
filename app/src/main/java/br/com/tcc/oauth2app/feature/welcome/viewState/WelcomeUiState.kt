package br.com.tcc.oauth2app.feature.welcome.viewState

data class WelcomeUiState(
    val welcomeState: WelcomeState
) {
    companion object {
        fun initialState() = WelcomeUiState(
            welcomeState = WelcomeState.Initial
        )
    }
}

sealed interface WelcomeState {
    object Initial : WelcomeState
    object Loading : WelcomeState
    data class Error(val message: String) : WelcomeState
    data class Success(val urlAuthentication: String) : WelcomeState
}