package br.com.tcc.oauth2app.data.remote.repository

import AccountDomain
import br.com.tcc.oauth2app.data.remote.ApiService
import br.com.tcc.oauth2app.data.remote.handlers.handleRequestException
import br.com.tcc.oauth2app.data.remote.handlers.handleRequestSuccess
import br.com.tcc.oauth2app.data.remote.model.toDomain
import br.com.tcc.oauth2app.data.remote.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): AccountRepository {
    override suspend fun getAccount(): NetworkResult<AccountDomain> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAccount()
                response.handleRequestSuccess { it.toDomain() }
            } catch (e: Throwable) {
                e.handleRequestException { it }
            }
        }
    }
}