package com.example.baseprojectandroid.di

import androidx.viewbinding.BuildConfig
import com.example.baseprojectandroid.data.ErrorResponse
import com.example.baseprojectandroid.local.LocalStorage
import com.example.baseprojectandroid.server.ApiClient
import com.example.baseprojectandroid.utils.Constants.BASE_URL
import com.example.baseprojectandroid.utils.Constants.TIME_OUT
import com.example.baseprojectandroid.utils.Logger
import com.example.baseprojectandroid.utils.NetworkUtils
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.internal.EMPTY_RESPONSE
import okhttp3.internal.userAgent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitInterface(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(httpClient).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create()).build()
    }

    @Provides
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor, request: Interceptor
    ): OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(request)
        .followRedirects(true).connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .callTimeout(TIME_OUT, TimeUnit.SECONDS).build()

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    fun listenerResponse(storage: LocalStorage?) = Interceptor { chain ->
        var request = chain.request()
        val build = request.newBuilder()
            .header("Content-Type", "application/json")
            .header("User-Agent", userAgent)

        val url = request.url
        val stringUrl = url.toString()
        request = build.build()
        val host = url.host
        try {
            val response = connect(chain, request, storage)
            response
        } catch (e: Exception) {
            //TODO
            Logger.d("------Exception", e.message)
            getErrorResponse(request)
        } catch (e: Error) {
            //TODO
            getErrorResponse(request)
        }
    }

    private fun connect(
        chain: Interceptor.Chain, request: Request, localStorage: LocalStorage?
    ): Response {
        var errorr: Exception? = null
        val rawUrl = request.url
        val url = rawUrl.toString()
        var timeLoad = -1
        timeLoad += 1
        try {
            val res = chain.withConnectTimeout(TIME_OUT.toInt() ushr 1, TimeUnit.MILLISECONDS)
                .proceed(request)
            if (res.isSuccessful) {
                return res
            }
        } catch (e: Exception) {
            errorr = e
        }
        timeLoad += 1
        val hasNetwork = NetworkUtils.isNetworkConnected()
        val message = errorr?.message?.lowercase(Locale.getDefault()) ?: ""
        if (!hasNetwork || message.contains("canceled")) {
            //TODO
            throw ErrorResponse(
                code = -1, message = message, cause = errorr?.cause, isOnline = hasNetwork
            ).also {
                it.stackTrace = errorr?.stackTrace ?: arrayOf()
            }
        } else {
            throw ErrorResponse(
                code = -1,
                message = message,
                cause = errorr?.cause,
                isOnline = hasNetwork,
                url = url
            )
        }
    }

    fun getErrorResponse(request: Request): Response {
        return Response.Builder().code(200).body(EMPTY_RESPONSE).message("Empty Response")
            .addHeader("Content-Type", "application/json").request(request)
            .protocol(Protocol.HTTP_2).build()
    }

    fun get(url: String, response: ((String) -> Unit), failure: ((Int) -> Unit)? = null) {
        val request = Request.Builder().url(url).addHeader("user-agent", userAgent).build()
        val client = provideOkHttpClient(provideLoggingInterceptor(), listenerResponse(null))
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                failure?.invoke(-1)
            }

            override fun onResponse(call: Call, res: Response) {
                val code = res.code
                val body = res.body
                if (res.isSuccessful && body != null) {
                    response.invoke(body.string())
                } else {
                    failure?.invoke(code)
                }
            }
        })
    }

}