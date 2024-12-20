package com.sparky.remotef1

import com.google.gson.annotations.SerializedName

data class ConfigType (
    val fileName: String,
    val config: String,
)

data class ConfigContent (
    val ip: String,
    val port: String,
    val rangeLF: Float,
    val rangeLB: Float,
    val rangeRF: Float,
    val rangeRB: Float,
)