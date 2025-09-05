package br.com.tcc.oauth2app.data.remote.exception

import okhttp3.ResponseBody

data class APIThrowable(
    override val message: String?,
    val code: Int,
    val errorBody: ResponseBody?
): Throwable()