package com.xan.nrcdemo.passport

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xan.nrcdemo.data.*
import com.xan.nrcdemo.data.passportData.RequestPassportModel
import com.xan.nrcdemo.data.passportData.ResponsePassportModel
import com.xan.nrcdemo.network.ConnectRetrofitService1
import com.xan.nrcdemo.network.ConnectRetrofitService2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class passportViewModel:ViewModel() {
    private var sharedPreferences: SharedPreferences? = null
    var context: Context? = null
    var strList = ArrayList<String>()
    var showLoading = MutableLiveData<Boolean>()
    var BackrequestModel = MutableLiveData<RequestPassportModel>()
    var myanResponseModel = MutableLiveData<ResponsePassportModel>()
    var responseModel = MutableLiveData<MyanglishResponModel>()

    init {
        showLoading.value = false
    }


    fun extractMyanmar() {
        sharedPreferences = context!!.getSharedPreferences(
            "nrcdemo",
            Context.MODE_PRIVATE
        )
        showLoading.value = true
        Log.d("strlist", strList?.size.toString())
        ConnectRetrofitService1.RETROFIT_SERVICE.extractPassport(
            sharedPreferences?.getString("Token", "nothing")!!, BackrequestModel.value!!
        ).enqueue(object : Callback<ResponsePassportModel> {
            override fun onFailure(call: Call<ResponsePassportModel>, t: Throwable) {
                Log.d("##Fail", "Call" + call.toString())
                showLoading.value = false
            }

            override fun onResponse(
                call: Call<ResponsePassportModel>,
                response: Response<ResponsePassportModel>
            ) {
                if (response.isSuccessful) {
                    Log.d("##RealSuccess", "Call")
                    showLoading.value = false
                    myanResponseModel.value = response.body()
                } else {
                    showLoading.value = false
                }
            }

        })
    }

    fun setMyanRequest(image: String) {
        BackrequestModel.value = RequestPassportModel(image, "GENERAL")
    }

}