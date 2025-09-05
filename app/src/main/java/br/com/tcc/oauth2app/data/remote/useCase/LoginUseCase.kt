package br.com.tcc.oauth2app.data.remote.useCase

import LoginDomain
import TokenDomain
import br.com.tcc.oauth2app.data.remote.model.RequestDataLogin
import br.com.tcc.oauth2app.data.remote.model.RequestDataToken
import br.com.tcc.oauth2app.data.remote.repository.LoginRepository
import br.com.tcc.oauth2app.data.remote.utils.NetworkResult
import javax.inject.Inject

interface LoginUseCase {
    suspend operator fun invoke(loginRequestData: RequestDataLogin): NetworkResult<LoginDomain>
    suspend fun getToken(tokenRequestDataToken: RequestDataToken): NetworkResult<TokenDomain>
}

class LoginUseCaseImpl @Inject constructor(
    private val loginRepository: LoginRepository
): LoginUseCase {
    override suspend fun invoke(loginRequestData: RequestDataLogin): NetworkResult<LoginDomain> {
        return loginRepository.login(loginRequestData)
    }

    override suspend fun getToken(tokenRequestDataToken: RequestDataToken): NetworkResult<TokenDomain> {
        return loginRepository.token(tokenRequestDataToken)
    }
}