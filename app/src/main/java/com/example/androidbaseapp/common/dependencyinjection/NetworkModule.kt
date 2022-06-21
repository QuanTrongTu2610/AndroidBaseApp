package com.example.androidbaseapp.common.dependencyinjection

import android.content.Context
import com.example.androidbaseapp.BuildConfig
import com.example.androidbaseapp.NetworkConfig
import com.example.androidbaseapp.data.remote.CovidDynamicApiService
import com.example.androidbaseapp.data.remote.NewspaperDynamicApiService
import com.example.androidbaseapp.data.remote.http.NetWorkRetryInterceptor
import com.example.androidbaseapp.data.remote.http.NetworkAuthenticationInterceptor
import com.example.androidbaseapp.data.remote.http.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @CommonInterceptorOkHttpClient
    @Singleton
    fun provideCommonOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val networkInterceptor = NetworkConnectionInterceptor(context = context)
        val netWorkRetryInterceptor = NetWorkRetryInterceptor()
        return OkHttpClient.Builder()
            .readTimeout(NetworkConfig.READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(NetworkConfig.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(NetworkConfig.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(networkInterceptor)
            .addInterceptor(netWorkRetryInterceptor)
            .apply {
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = HttpLoggingInterceptor()
                        .apply { level = HttpLoggingInterceptor.Level.BODY }
                    addInterceptor(loggingInterceptor)
                }
            }
            .build()
    }

    @Provides
    @AuthInterceptorOkHttpClient
    @Singleton
    fun provideAuthOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val networkInterceptor = NetworkConnectionInterceptor(context = context)
        val netWorkRetryInterceptor = NetWorkRetryInterceptor()
        val netWorkAuthInterceptor = NetworkAuthenticationInterceptor()
        return OkHttpClient.Builder()
            .readTimeout(NetworkConfig.READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(NetworkConfig.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(NetworkConfig.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(networkInterceptor)
            .addInterceptor(netWorkRetryInterceptor)
            .addInterceptor(netWorkAuthInterceptor)
            .apply {
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = HttpLoggingInterceptor()
                        .apply { level = HttpLoggingInterceptor.Level.BODY }
                    addInterceptor(loggingInterceptor)
                }
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideCovidDynamicApiService(
        @CommonInterceptorOkHttpClient okHttpClient: OkHttpClient
    ): CovidDynamicApiService {
        val builder = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
        return CovidDynamicApiService(builder)
    }

    @Provides
    @Singleton
    fun provideNewspaperApiService(
        @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient
    ): NewspaperDynamicApiService {
        val builder = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
        return NewspaperDynamicApiService(builder)
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CommonInterceptorOkHttpClient