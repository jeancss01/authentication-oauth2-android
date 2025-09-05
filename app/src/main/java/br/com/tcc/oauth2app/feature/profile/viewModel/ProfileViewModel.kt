package br.com.tcc.oauth2app.feature.profile.viewModel

import AccountDomain
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tcc.oauth2app.data.remote.model.toAccountUi
import br.com.tcc.oauth2app.data.remote.useCase.AccountUseCase
import br.com.tcc.oauth2app.data.remote.utils.onError
import br.com.tcc.oauth2app.data.remote.utils.onSuccess
import br.com.tcc.oauth2app.feature.profile.viewState.ProfileState
import br.com.tcc.oauth2app.feature.profile.viewState.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(
    private val useCase: AccountUseCase
) : ViewModel()  {

    private val _profileUiState = MutableLiveData(ProfileUiState.initialState())
    val profileViewState: LiveData<ProfileUiState> get() = _profileUiState

    fun getAccount() {
        updateState(ProfileState.Loading)
        viewModelScope.launch {
            useCase.getAccount().onSuccess { account ->
                handleSuccess(account)
            }.onError { throwable ->
                handleError(throwable)
            }
        }
    }
    private fun updateState(newState: ProfileState) {
        _profileUiState.let {
            it.value = it.value?.copy( profileState = newState)
        }
    }

    private fun handleSuccess(account: AccountDomain) {
        updateState(ProfileState.Success(account.toAccountUi()))
    }

    private fun handleError(throwable: Throwable) {
        updateState(ProfileState.Error(throwable.message ?: "Unknown error"))
    }


}