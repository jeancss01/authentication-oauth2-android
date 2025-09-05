package br.com.tcc.oauth2app.common.di

import android.content.Context
import br.com.tcc.oauth2app.BuildConfig
import br.com.tcc.oauth2app.common.SecureStorage
import br.com.tcc.oauth2app.data.remote.ApiService
import br.com.tcc.oauth2app.data.remote.NetworkURLs
import br.com.tcc.oauth2app.data.remote.interceptors.CustomInterceptorImpl
import br.com.tcc.oauth2app.data.remote.repository.LoginRepository
import br.com.tcc.oauth2app.data.remote.repository.LoginRepositoryImpl
import br.com.tcc.oauth2app.data.remote.repository.AccountRepository
import br.com.tcc.oauth2app.data.remote.repository.AccountRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(
        interceptor: Interceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
        }
        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkURLs.BASE_URL_PRD)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providerRepository(
        apiService: ApiService
    ) : LoginRepository {
        return LoginRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun providerAccountRepository(
        apiService: ApiService
    ) : AccountRepository {
        return AccountRepositoryImpl(apiService)
    }

    @Singleton
    @Provides
    fun provideRequestInterceptor(
        secureStorage: SecureStorage,
        @ApplicationContext context: Context
    ): Interceptor {
        return CustomInterceptorImpl(secureStorage, context)
    }
}