package com.xan.nrcdemo.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyBackRespondModel (
    @SerializedName("result")
    var result: backResult
): Serializable

data class backResult(
    @SerializedName("side")
    var side: String?,
    @SerializedName("card_id")
    var card_id: String?,
    @SerializedName("nrc_id_back")
    var nrc_id_back: String?,
    @SerializedName("profession")
    var profession: String?,
    @SerializedName("address")
    var address: String?,
    @SerializedName("class")
    var Class: String?
)
