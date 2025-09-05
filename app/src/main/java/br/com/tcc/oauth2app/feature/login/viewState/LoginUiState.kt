package br.com.tcc.oauth2app.feature.login.viewState
import br.com.tcc.oauth2app.common.ValidatorStateType

data class LoginUiState(
    val inputEmailState: ValidatorStateType,
    val inputPasswordState: ValidatorStateType,
    val loginState: LoginState
) {
    companion object {
        fun initialState() = LoginUiState(
            inputEmailState = ValidatorStateType.EMPTY,
            inputPasswordState = ValidatorStateType.EMPTY,
            loginState = LoginState.EmptyInputs
        )
    }
}

sealed interface LoginState {
    object EmptyInputs : LoginState
    object ValidInputs : LoginState
    data class InvalidInputs(val userInputError: Int?) : LoginState
    object Loading : LoginState
}