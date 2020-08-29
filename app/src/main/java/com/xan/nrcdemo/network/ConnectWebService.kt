package com.xan.nrcdemo.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.xan.nrcdemo.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
private const val BASE_URL = "https://iam.ap-southeast-1.myhuaweicloud.com/" // development url
private const val BASE_URL1 = "https://ocr.ap-southeast-1.myhuaweicloud.com/" // development url
private const val BASE_URL2 = "https://translation.googleapis.com/language/translate/" // development url

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

val retrofit = Retrofit.Builder()
    .client(makeHttpClient())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

object ConnectRetrofitService {
    val RETROFIT_SERVICE: ApiServices by lazy {
        retrofit.create(ApiServices::class.java)
    }
}

val retrofit1 = Retrofit.Builder()
    .client(makeHttpClient())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL1)
    .build()

object ConnectRetrofitService1 {
    val RETROFIT_SERVICE: ApiServices by lazy {
        retrofit1.create(ApiServices::class.java)
    }
}

val retrofit2 = Retrofit.Builder()
    .client(makeHttpClient())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL2)
    .build()

object ConnectRetrofitService2 {
    val RETROFIT_SERVICE: ApiServices by lazy {
        retrofit2.create(ApiServices::class.java)
    }
}

private fun makeHttpClient() = OkHttpClient.Builder()
    .addInterceptor(AuthorizationInterceptor())
    .addNetworkInterceptor(loggingInterceptor())
    .build()

fun loggingInterceptor() = HttpLoggingInterceptor().apply {
    level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}
