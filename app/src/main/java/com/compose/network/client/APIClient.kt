package com.compose.network.client

import com.compose.BuildConfig
import com.compose.network.restClient.RestClient
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val CONNECTION_TIMEOUT = 30_000L

class APIClient : KoinComponent {

    private val stethoInterceptor: StethoInterceptor? by lazy {
        if (BuildConfig.DEBUG) StethoInterceptor() else null
    }

    private val headerMap: HashMap<String, String> by lazy {
        hashMapOf("Content-Type" to "application/json")
    }

    val retrofitClient: RestClient by lazy {
        retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(RestClient::class.java)
    }

    private val okHttpClient: OkHttpClient
        get() {
            okHttpClientBuilder.addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                headerMap.forEach {
                    builder.removeHeader(it.key)
                    builder.addHeader(it.key, it.value)
                }
                chain.proceed(builder.build())
            }
            return okHttpClientBuilder.build()
        }

    private val retrofitBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
    }

    private val okHttpClientBuilder: OkHttpClient.Builder by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        stethoInterceptor?.let {
            builder.addNetworkInterceptor(it)
        }
        builder
    }


}

