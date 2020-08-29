package com.xan.nrcdemo.data.passportData

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ResponsePassportModel (
    @SerializedName("result")
    var result: passportResult
): Serializable

data class passportResult(
    @SerializedName("country_code")
    var country_code: String?,
    @SerializedName("surname")
    var surname: String?,
    @SerializedName("given_name")
    var given_name: String?,
    @SerializedName("passport_number")
    var passport_number: String?,
    @SerializedName("date_of_birth")
    var date_of_birth: String?,
    @SerializedName("sex")
    var sex: String?,
    @SerializedName("date_of_expiry")
    var date_of_expiry: String?,
    @SerializedName("machine_code")
    var machine_code: String?,
    @SerializedName("machine_code2")
    var machine_code2: String?
)