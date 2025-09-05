package br.com.tcc.oauth2app.data.remote.handlers

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun toRequestBody(body: Any): RequestBody {
    return Gson().toJson(body).toRequestBody("application/json".toMediaTypeOrNull())
}

fun Any.parseToRequestBody(): RequestBody = toRequestBody(this)
