package br.com.tcc.oauth2app.common.di

import br.com.tcc.oauth2app.data.remote.useCase.AccountUseCase
import br.com.tcc.oauth2app.data.remote.useCase.AccountUseCaseImpl
import br.com.tcc.oauth2app.data.remote.useCase.LoginUseCase
import br.com.tcc.oauth2app.data.remote.useCase.LoginUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindLoginUseCase(loginUseCaseImpl: LoginUseCaseImpl): LoginUseCase

    @Binds
    @Singleton
    fun bindAccountUseCase(accountUseCaseImpl: AccountUseCaseImpl): AccountUseCase
}