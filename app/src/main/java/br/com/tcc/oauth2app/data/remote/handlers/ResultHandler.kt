package br.com.tcc.oauth2app.data.remote.handlers

import br.com.tcc.oauth2app.data.remote.exception.APIThrowable
import br.com.tcc.oauth2app.data.remote.utils.NetworkResult
import br.com.tcc.oauth2app.data.remote.utils.asNetworkResultError
import retrofit2.Response

inline fun <reified T, reified R> Response<T>.handleRequestSuccess(mapper: (T) -> R): NetworkResult<R> {
    if (!this.isSuccessful) {
        throw APIThrowable(
            message = "Tente novamente mais tarde",
            code = this.code(),
            errorBody = errorBody()
        )
    }
    return NetworkResult.SuccessResult(body()?.run { mapper(this) } ?: Unit as R)
}

fun Throwable.handleRequestException(mapper: (APIThrowable) -> Throwable) =
    if (this is APIThrowable) {
        when (this.code) {
            400 -> {
                mapper(this).asNetworkResultError()
            }
            else -> {
                asNetworkResultError()
            }
        }
    } else {
        asNetworkResultError()
    }
