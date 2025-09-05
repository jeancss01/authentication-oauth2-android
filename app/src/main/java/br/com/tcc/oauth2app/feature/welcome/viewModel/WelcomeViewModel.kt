package br.com.tcc.oauth2app.feature.welcome.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.tcc.oauth2app.common.PKCEUtil
import br.com.tcc.oauth2app.common.SecureStorage
import br.com.tcc.oauth2app.data.remote.NetworkURLs
import br.com.tcc.oauth2app.data.remote.OAuth2Constants
import br.com.tcc.oauth2app.feature.welcome.viewState.WelcomeState
import br.com.tcc.oauth2app.feature.welcome.viewState.WelcomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val secureStorage: SecureStorage
): ViewModel()  {

    private val _welcomeUiState = MutableLiveData(WelcomeUiState.initialState())

    val welcomeUiState: LiveData<WelcomeUiState> get() = _welcomeUiState

    fun getAuthenticationUrl() {
        updateState(WelcomeState.Loading)
        updateState(WelcomeState.Success(getURLAuthentication()))
    }

    private fun updateState(newState: WelcomeState) {
        _welcomeUiState.let {
            it.value = it.value?.copy( welcomeState = newState)
        }
    }

    private fun getURLAuthentication(): String {
        val codeVerifier = PKCEUtil.generateCodeVerifier()
        val codeChallenge = PKCEUtil.generateCodeChallenge(codeVerifier)

        secureStorage.saveCodeVerifier(codeVerifier)
        secureStorage.saveCodeChallenge(codeChallenge)

        val clientId = OAuth2Constants.CLIENT_ID
        val redirectUri = OAuth2Constants.REDIRECT_URI
        val codeChallengeMethod = OAuth2Constants.CODE_CHALLENGE_METHOD
        val url = "${NetworkURLs.BASE_URL_PRD}oauth/authorize?clientId=$clientId&redirectUri=$redirectUri&codeChallenge=$codeChallenge&codeChallengeMethod=$codeChallengeMethod"

        return url
    }
}