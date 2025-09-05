package br.com.tcc.oauth2app.data.remote.repository

import AccountDomain
import br.com.tcc.oauth2app.data.remote.utils.NetworkResult

interface AccountRepository {
    suspend fun getAccount(): NetworkResult<AccountDomain>
}
