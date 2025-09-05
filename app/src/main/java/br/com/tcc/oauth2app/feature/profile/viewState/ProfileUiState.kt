package br.com.tcc.oauth2app.feature.profile.viewState

data class ProfileUiState(
    val profileState: ProfileState
) {
    companion object {
        fun initialState() = ProfileUiState(
            profileState = ProfileState.Initial
        )
    }
}

sealed interface ProfileState {
    object Initial : ProfileState
    object Loading : ProfileState
    data class Error(val message: String) : ProfileState
    data class Success(val account: AccountUi) : ProfileState
}

data class AccountUi(
    val id: String,
    val name: String,
    val email: String,
    val country: String,
    val city: String,
    val state: String
) {
    companion object {
        fun initialState() = AccountUi(
            id = "",
            name = "",
            email = "",
            country = "",
            city = "",
            state = ""
        )
    }
}