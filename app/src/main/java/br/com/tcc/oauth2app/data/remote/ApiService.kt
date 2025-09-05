package br.com.tcc.oauth2app.data.remote

import br.com.tcc.oauth2app.data.remote.model.ResponseAccount
import br.com.tcc.oauth2app.data.remote.model.ResponseLogin
import br.com.tcc.oauth2app.data.remote.model.ResponseToken
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("oauth/login")
    suspend fun login( @Body requestBody: RequestBody): Response<ResponseLogin>

    @POST("oauth/token")
    suspend fun token(@Body requestBody: RequestBody): Response<ResponseToken>

    @GET("account")
    suspend fun getAccount(): Response<ResponseAccount>

    @POST("oauth/token")
    fun refreshToken(@Body requestBody: RequestBody): Call<ResponseToken>
}