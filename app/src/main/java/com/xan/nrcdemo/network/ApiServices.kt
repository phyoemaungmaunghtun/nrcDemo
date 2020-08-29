package com.xan.nrcdemo.network

import com.xan.nrcdemo.data.*
import com.xan.nrcdemo.data.passportData.RequestPassportModel
import com.xan.nrcdemo.data.passportData.ResponsePassportModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiServices {
    @POST("v2?key=AIzaSyC1r46Rt9YPqTG30qKfkPgA0EQMBN1NFCQ&target=en")
    fun translateToMyanglish(@Body Request: MyanglishRequestModel): Call<MyanglishResponModel>

    @POST("v1.0/myanmar-id-card")
    fun extractMyanmar(@Header("X-Auth-Token") token: String, @Body Request: BackMyanglishRequestModel): Call<MyanResponseModel>

    @POST("v1.0/myanmar-id-card")
    fun extractBackMyanmar(@Header("X-Auth-Token") token: String, @Body Request: BackMyanglishRequestModel): Call<MyBackRespondModel>

    @POST("v1.0/ocr/passport")
    fun extractPassport(@Header("X-Auth-Token") token: String, @Body Request: RequestPassportModel): Call<ResponsePassportModel>


    @POST("v3/auth/tokens")
    fun refreshedToken(@Body Request: TokenRequestModel): Call<ResponseBody>

}