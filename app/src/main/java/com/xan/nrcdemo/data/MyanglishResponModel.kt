package com.xan.nrcdemo.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyanglishResponModel(
    @SerializedName("data")
    val data: Data?
) : Serializable

data class Data(
    @SerializedName("translations")
    var translations: List<translateText>
): Serializable

data class translateText(
    @SerializedName("translatedText")
    var translatedText: String,
    @SerializedName("detectedSourceLanguage")
    var detectedSourceLanguage: String
): Serializable


