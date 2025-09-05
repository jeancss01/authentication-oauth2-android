package br.com.tcc.oauth2app.data.remote.interceptors

import android.content.Context
import android.content.Intent
import br.com.tcc.oauth2app.MainActivity
import br.com.tcc.oauth2app.common.SecureStorage
import br.com.tcc.oauth2app.data.remote.ApiService
import br.com.tcc.oauth2app.data.remote.Headers
import br.com.tcc.oauth2app.data.remote.NetworkURLs
import br.com.tcc.oauth2app.data.remote.handlers.toRequestBody
import br.com.tcc.oauth2app.data.remote.model.RequestDataRefreshToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CustomInterceptorImpl @Inject constructor(
    private val secureStorage: SecureStorage,
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Adiciona o token de acesso ao cabeçalho da requisição original
        val requestWithToken = addTokenToRequest(originalRequest)

        // Executa a requisição
        val response = chain.proceed(requestWithToken)

        // Se a resposta for 401 (Unauthorized), tente renovar o token
        if (response.code == 401) {
            // Verifica se o token está expirado e se há um refresh token disponível
            if (secureStorage.isTokenExpired() && secureStorage.getRefreshToken() != null) {
                response.close()

                // Aqui você deve implementar a lógica para obter um novo token usando o refresh token
                val newTokenObtained = refreshToken()

                if (newTokenObtained) {
                    // Se conseguiu um novo token, refaz a requisição original com o novo token
                    val newRequest = addTokenToRequest(originalRequest)
                    return chain.proceed(newRequest)
                }
            }

            // Se não conseguiu renovar o token, redireciona para a tela de login
            //redirectToLogin()
        }

        return response
    }

    private fun addTokenToRequest(request: Request): Request {
        val token = secureStorage.getToken()
        return if (!token.isNullOrEmpty()) {
            val newRequest = request.newBuilder()
                .addHeader(Headers.HEADER_AUTHORIZATION_KEY, "Bearer $token")
                .build()
            newRequest
        } else {
            request
        }
    }

    private fun refreshToken(): Boolean {
        // Implemente a lógica para obter um novo token usando o refresh token
        // Isso geralmente envolve uma chamada síncrona para a API de autenticação
        // Retorne true se conseguir um novo token, false caso contrário

        try {
            val refreshToken = secureStorage.getRefreshToken() ?: return false

            // Crie um cliente Retrofit específico para renovação de token (sem interceptors)
            val authService = createAuthService()

            val requestDataRefreshToken = RequestDataRefreshToken(
                refreshToken = refreshToken
            )

            // Faça uma chamada síncrona para renovar o token
            val response = authService.refreshToken(toRequestBody(requestDataRefreshToken)).execute()

            if (response.isSuccessful && response.body() != null) {
                val tokenResponse = response.body()!!
                secureStorage.saveTokenWithExpiration(tokenResponse.accessToken, tokenResponse.expiresIn)
                secureStorage.saveRefreshToken(tokenResponse.refreshToken)
                return true
            }

            return false
        } catch (e: Exception) {
            return false
        }
    }

    private fun redirectToLogin() {
        // Limpa os tokens
        secureStorage.clearTokens()

        // Redireciona para a tela de login usando um Intent com FLAG_ACTIVITY_NEW_TASK
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
         context.startActivity(intent)
    }

    private fun createAuthService(): ApiService {
        // Crie um cliente Retrofit específico para renovação de token
        // Este cliente não deve ter interceptors para evitar loops infinitos
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(NetworkURLs.BASE_URL_PRD)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}