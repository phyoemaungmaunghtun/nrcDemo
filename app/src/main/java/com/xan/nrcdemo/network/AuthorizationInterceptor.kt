package com.xan.nrcdemo.network

import android.util.Log.d
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by Zaw Zaw Htet on 26,May,2020
 */
class AuthorizationInterceptor : Interceptor {

    lateinit var response: Response

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val builder = request.newBuilder()
        response = chain.proceed(request)
       /* if (RefreshTokenDataProvider().getToken() != null) {
            val token = RefreshTokenDataProvider().getToken()
            setAuthHeader(builder, token)
            builder.header("Accept", "application/json")
            request = builder.build()
            response = chain.proceed(request)
            if (response.code == 401) {
                refreshToken()
            }
        } else {
            response = chain.proceed(request)
        }*/
        return response
    }

    private fun setAuthHeader(builder: Request.Builder, token: String?) {
        builder.header("Authorization", String.format("Bearer %s", token))
    }

    /*private fun refreshToken() {
        ConnectRetrofitService.RETROFIT_SERVICE
            .refreshedToken(RefreshTokenDataProvider().getRefreshToken().toString())
            .enqueue(
                object : Callback<RefreshTokenServerResponse> {
                    override fun onFailure(call: Call<RefreshTokenServerResponse>, t: Throwable) {
                        d("##Failure##", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<RefreshTokenServerResponse>,
                        response: retrofit2.Response<RefreshTokenServerResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.data == null) {
                                DataHolder.refreshTokenFlag.value = false
                            } else {
                                RefreshTokenDataProvider().storeToken(response.body()?.data!!.access_token)
                                DataHolder.refreshTokenFlag.value = true
                            }
                        } else {
                            DataHolder.refreshTokenFlag.value = false
                            d("##Unsucessful##", response.message())
                        }
                    }
                }
            )
    }*/

}