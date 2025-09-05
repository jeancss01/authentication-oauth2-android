package br.com.tcc.oauth2app.data.remote.repository

import LoginDomain
import TokenDomain
import br.com.tcc.oauth2app.data.remote.model.RequestDataLogin
import br.com.tcc.oauth2app.data.remote.model.RequestDataToken
import br.com.tcc.oauth2app.data.remote.utils.NetworkResult

interface LoginRepository {
    suspend fun login(loginRequestData: RequestDataLogin): NetworkResult<LoginDomain>
    suspend fun token(requestDataToken: RequestDataToken): NetworkResult<TokenDomain>
}