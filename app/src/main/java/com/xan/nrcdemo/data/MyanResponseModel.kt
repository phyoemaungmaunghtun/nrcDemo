package com.xan.nrcdemo.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyanResponseModel(
    @SerializedName("result")
    var result: result
):Serializable

data class result(
    @SerializedName("side")
    var side: String?,
    @SerializedName("nrc_id")
    var nrc_id: String?,
    @SerializedName("issue_date")
    var issue_date: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("father_name")
    var father_name: String?,
    @SerializedName("birth")
    var birth: String?,
    @SerializedName("bloodlines_religion")
    var bloodlines_religion: String?,
    @SerializedName("height")
    var height: String?,
    @SerializedName("blood_group")
    var blood_group: String?,
    @SerializedName("class")
    var Class: String?
)
