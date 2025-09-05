package br.com.tcc.oauth2app.data.remote.repository


import LoginDomain
import TokenDomain
import br.com.tcc.oauth2app.data.remote.ApiService
import br.com.tcc.oauth2app.data.remote.handlers.handleRequestException
import br.com.tcc.oauth2app.data.remote.handlers.handleRequestSuccess
import br.com.tcc.oauth2app.data.remote.handlers.toRequestBody
import br.com.tcc.oauth2app.data.remote.model.RequestDataLogin
import br.com.tcc.oauth2app.data.remote.model.RequestDataToken
import br.com.tcc.oauth2app.data.remote.model.toDomain
import br.com.tcc.oauth2app.data.remote.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : LoginRepository {
    override suspend fun login(loginRequestData: RequestDataLogin): NetworkResult<LoginDomain> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(toRequestBody(loginRequestData))
                response.handleRequestSuccess { it.toDomain() }

            } catch (e: Throwable) {
                e.handleRequestException {it}
            }
        }
    }

    override suspend fun token(requestDataToken: RequestDataToken): NetworkResult<TokenDomain> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.token(toRequestBody(requestDataToken))
                response.handleRequestSuccess { it.toDomain() }

            } catch (e: Throwable) {
                e.handleRequestException {it}
            }
        }
    }
}