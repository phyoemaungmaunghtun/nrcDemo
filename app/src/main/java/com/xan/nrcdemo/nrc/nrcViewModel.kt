package com.xan.nrcdemo.nrc

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xan.nrcdemo.data.*
import com.xan.nrcdemo.network.ConnectRetrofitService1
import com.xan.nrcdemo.network.ConnectRetrofitService2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class nrcViewModel : ViewModel() {
    private var sharedPreferences: SharedPreferences? = null
    var context: Context? = null
    var strList = ArrayList<String>()
    var showLoading = MutableLiveData<Boolean>()
    var BackrequestModel = MutableLiveData<BackMyanglishRequestModel>()
    var FrontrequestModel = MutableLiveData<BackMyanglishRequestModel>()
    var myanResponseModel = MutableLiveData<MyanResponseModel>()
    var responseModel = MutableLiveData<MyanglishResponModel>()
    var backResponModel = MutableLiveData<MyBackRespondModel>()

    init {
        showLoading.value = false
    }

    fun changeLanguage() {
        showLoading.value = true
        Log.d("strlist", strList?.size.toString())
        ConnectRetrofitService2.RETROFIT_SERVICE.translateToMyanglish(
            MyanglishRequestModel(
                strList!!
            )
        ).enqueue(object : Callback<MyanglishResponModel> {
            override fun onFailure(call: Call<MyanglishResponModel>, t: Throwable) {
                Log.d("##Fail", "Call" + call.toString())
                showLoading.value = false
            }

            override fun onResponse(
                call: Call<MyanglishResponModel>,
                response: Response<MyanglishResponModel>
            ) {
                if (response.isSuccessful) {
                    Log.d("##RealSuccess", "Call")
                    showLoading.value = false
                    responseModel.value = response.body()
                } else {
                    showLoading.value = false
                }
            }

        })
    }

    fun setRequestModel1() {
        if(strList.size != 0){
            strList.clear()
        }
        strList.add(myanResponseModel.value?.result?.name!!)
        strList.add(myanResponseModel.value?.result?.father_name!!)
        strList.add(myanResponseModel.value?.result?.nrc_id!!)
        strList.add(myanResponseModel.value?.result?.birth!!)
    }

    fun setRequestModel2() {
            strList?.add(backResponModel.value?.result?.card_id!!)
            strList?.add(backResponModel.value?.result?.profession!!)
            strList?.add(backResponModel.value?.result?.address!!)
    }


    fun extractMyanmar() {
        sharedPreferences = context!!.getSharedPreferences(
            "nrcdemo",
            Context.MODE_PRIVATE
        )
        showLoading.value = true
        Log.d("strlist", strList?.size.toString())
        ConnectRetrofitService1.RETROFIT_SERVICE.extractMyanmar(
            sharedPreferences?.getString("Token", "nothing")!!, FrontrequestModel.value!!
        ).enqueue(object : Callback<MyanResponseModel> {
            override fun onFailure(call: Call<MyanResponseModel>, t: Throwable) {
                Log.d("##Fail", "Call" + call.toString())
                showLoading.value = false
            }

            override fun onResponse(
                call: Call<MyanResponseModel>,
                response: Response<MyanResponseModel>
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

    fun extractBackMyanmar() {
        sharedPreferences = context!!.getSharedPreferences(
            "nrcdemo",
            Context.MODE_PRIVATE
        )
        showLoading.value = true
        Log.d("strlist", strList?.size.toString())
        ConnectRetrofitService1.RETROFIT_SERVICE.extractBackMyanmar(
            sharedPreferences?.getString("Token", "nothing")!!, BackrequestModel.value!!
        ).enqueue(object : Callback<MyBackRespondModel> {
            override fun onFailure(call: Call<MyBackRespondModel>, t: Throwable) {
                Log.d("##Fail", "Call" + call.toString())
                showLoading.value = false
            }

            override fun onResponse(
                call: Call<MyBackRespondModel>,
                response: Response<MyBackRespondModel>
            ) {
                if (response.isSuccessful) {
                    Log.d("##RealSuccess", "Call")
                    showLoading.value = false
                    backResponModel.value = response.body()
                } else {
                    showLoading.value = false
                }
            }

        })
    }

    fun setBackMyanRequest(image: String) {
        BackrequestModel.value = BackMyanglishRequestModel(image, true)
    }

    fun setFrontMyanRequest(image: String) {
        FrontrequestModel.value = BackMyanglishRequestModel(image, true)
    }
}