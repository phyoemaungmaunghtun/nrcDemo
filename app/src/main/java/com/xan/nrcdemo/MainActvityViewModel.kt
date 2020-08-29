package com.xan.nrcdemo

import android.content.Context
import android.content.SharedPreferences
import android.text.format.Time
import android.util.Log.d
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xan.nrcdemo.data.*
import com.xan.nrcdemo.network.ConnectRetrofitService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActvityViewModel : ViewModel() {
    private var sharedPreferences: SharedPreferences? = null
    var today: Time? = null
    var context: Context? = null
    var strList = ArrayList<String>()
    var method = ArrayList<String>()
    var showLoading = MutableLiveData<Boolean>()
    var token_check = MutableLiveData<Boolean>()
    var requestTokenModel = MutableLiveData<TokenRequestModel>()
    var auth = MutableLiveData<auth>()
    var user = MutableLiveData<user>()
    var identity = MutableLiveData<identity>()
    var domain = MutableLiveData<domain>()
    var scope = MutableLiveData<scope>()
    var password = MutableLiveData<password>()
    var project = MutableLiveData<project>()
    var responseModel = MutableLiveData<MyanglishResponModel>()

    init {
        showLoading.value = false
        token_check.value = false
        today = Time(Time.getCurrentTimezone())
        today!!.setToNow()
    }

    fun takeToken() {
        showLoading.value = true
        d("strlist", strList?.size.toString())
        ConnectRetrofitService.RETROFIT_SERVICE.refreshedToken(
            requestTokenModel.value!!
        ).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                d("##Fail", "Call" + call.toString())
                showLoading.value = false
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    d("##RealSuccess", "Call")
                    showLoading.value = false
                    var token = response.headers().get("X-Subject-Token")
                    d("##token", "" + token)
                    sharedPreferences = context!!.getSharedPreferences(
                        "nrcdemo",
                        Context.MODE_PRIVATE
                    )
                    val editor = sharedPreferences?.edit()
                    editor?.putString("Token", token)
                    editor?.putString("day", today?.monthDay.toString())
                    editor?.apply()
                    token_check.value = true


                    //storeToken(token!!)
                } else {
                    showLoading.value = false
                }
            }

        })
    }

    fun setTokenRequest() {
        method.add("password")
        domain.value = domain("sainumtown")
        user.value = user("sainumtown", "1234567890!@#", domain.value!!)
        password.value = password(user.value!!)
        identity.value = identity(method, password.value!!)
        project.value = project("ap-southeast-1")
        scope.value = scope(project.value!!)
        auth.value = auth(identity.value!!, scope.value!!)
        requestTokenModel.value = TokenRequestModel(auth.value!!)
    }

}