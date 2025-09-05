package br.com.tcc.oauth2app.feature.login.viewModel

import TokenDomain
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tcc.oauth2app.R
import br.com.tcc.oauth2app.common.SecureStorage
import br.com.tcc.oauth2app.common.ValidatorStateType
import br.com.tcc.oauth2app.data.remote.model.RequestDataLogin
import br.com.tcc.oauth2app.data.remote.model.RequestDataToken
import br.com.tcc.oauth2app.data.remote.useCase.LoginUseCase
import br.com.tcc.oauth2app.data.remote.utils.onError
import br.com.tcc.oauth2app.data.remote.utils.onSuccess
import br.com.tcc.oauth2app.feature.login.viewState.LoginActionState
import br.com.tcc.oauth2app.feature.login.viewState.LoginState
import br.com.tcc.oauth2app.feature.login.viewState.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: LoginUseCase,
    private val secureStorage: SecureStorage
) : ViewModel() {

    private var codeChallenge: String = ""

    private var _loginViewState = MutableLiveData(LoginUiState.initialState())

    val loginViewState: LiveData<LoginUiState> get() = _loginViewState

    private var _loginActionState = Channel<LoginActionState>()
    val loginActionState = _loginActionState.receiveAsFlow()

    fun login(email: String, password: String) {
        updateSignInState(LoginState.Loading)
        viewModelScope.launch {
            useCase(
                RequestDataLogin(
                    email = email,
                    password = password,
                    codeChallenge = codeChallenge
                )
            ).onSuccess { loginResponse ->
                val authorizationCode = loginResponse.code
                if (authorizationCode.isNotEmpty()) {
                    fetchToken(authorizationCode)
                } else {
                    updateSignInState(LoginState.InvalidInputs(userInputError = R.string.str_login_invalido))
                }
            }.onError { throwable ->
                handleError(throwable)
            }
        }
    }

    private fun fetchToken(code: String) {
        updateSignInState(LoginState.Loading)
        viewModelScope.launch {
            useCase.getToken(
                RequestDataToken(
                    code = code,
                    codeVerifier = secureStorage.getCodeVerifier() ?: ""
                )
            ).onSuccess {
                handlerSuccess(it)
            }.onError { throwable ->
                handleError(throwable)
            }
        }
    }

    private fun updateSignInState(loginState: LoginState) {
        _loginViewState.let {
            it.value = it.value?.copy(loginState = loginState)
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        if (throwable.cause is NullPointerException) {
            _loginActionState.send(LoginActionState.InvalidCredentialsError("Erro de comunicação"))
        }else {
            _loginActionState.send(LoginActionState.InvalidCredentialsError(throwable.cause?.message ?: "Erro ao fazer login"))
        }
    }

    private suspend fun handlerSuccess(response: TokenDomain) {
        // Save user session
        _loginActionState.send(
            LoginActionState.ValidCredentials(true)
        )
        secureStorage.saveTokenWithExpiration(response.accessToken, response.expiresIn)
        secureStorage.saveRefreshToken(response.refreshToken)
    }

    fun validateInputs(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            updateSignInState(LoginState.ValidInputs)
            updateInputsState(ValidatorStateType.VALID, ValidatorStateType.VALID)
        } else {

            val inputUserState = when {
                email.isEmpty() -> ValidatorStateType.EMPTY
                password.isEmpty() -> ValidatorStateType.EMPTY
                else -> ValidatorStateType.VALID
            }

            if (inputUserState == ValidatorStateType.VALID) {
                updateSignInState(LoginState.ValidInputs)
            } else {
                updateSignInState(LoginState.InvalidInputs(userInputError = R.string.str_login_invalido))
            }
        }
    }

    fun setCodeChallenge(codeChallenge: String) {
        this.codeChallenge = codeChallenge
    }

    private fun updateInputsState(
        inputEmailState: ValidatorStateType,
        inputPasswordState: ValidatorStateType
    ) {
        _loginViewState.let {
            it.value = it.value?.copy(
                inputEmailState = inputEmailState,
                inputPasswordState = inputPasswordState
            )
        }
    }
}