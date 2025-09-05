package br.com.tcc.oauth2app.data.remote.utils

sealed class NetworkResult<out T> {
    class SuccessResult<T>(val data: T) : NetworkResult<T>()
    class ErrorResult(val message: Throwable) : NetworkResult<Nothing>()
}

suspend fun <T> NetworkResult<T>.onSuccess(action: suspend (value: T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.SuccessResult) action(this.data)
    return this
}

suspend fun <T> NetworkResult<T>.onError(action: suspend (error: Throwable) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.ErrorResult) action(Throwable(this.message))
    return this
}

fun Throwable.asNetworkResultError() = NetworkResult.ErrorResult(this)