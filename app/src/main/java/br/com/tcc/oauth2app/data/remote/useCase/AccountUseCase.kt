package br.com.tcc.oauth2app.data.remote.useCase

import AccountDomain
import br.com.tcc.oauth2app.data.remote.repository.AccountRepository
import br.com.tcc.oauth2app.data.remote.utils.NetworkResult
import javax.inject.Inject

interface AccountUseCase {
    suspend fun getAccount(): NetworkResult<AccountDomain>
}

class AccountUseCaseImpl @Inject constructor(
    private val accountRepository: AccountRepository
) : AccountUseCase {
    override suspend fun getAccount(): NetworkResult<AccountDomain> {
        return accountRepository.getAccount()
    }
}