package br.com.tcc.oauth2app.data.remote.model

import AccountDomain
import LoginDomain
import TokenDomain
import br.com.tcc.oauth2app.feature.profile.viewState.AccountUi

fun ResponseLogin.toDomain(): LoginDomain {
    return LoginDomain(
        code = this.code,
        expiresIn = this.expiresIn
    )
}

fun ResponseToken.toDomain(): TokenDomain {
    return TokenDomain(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        expiresIn = this.expiresIn
    )
}

fun ResponseAccount.toDomain(): AccountDomain {
    return AccountDomain(
        id = this.id,
        name = this.name,
        email = this.email,
        country = this.country,
        city = this.city,
        state = this.state
    )
}

fun AccountDomain.toAccountUi(): AccountUi {
    return AccountUi(
        id = this.id,
        name = this.name,
        email = this.email,
        country = this.country,
        city = this.city,
        state = this.state
    )
}