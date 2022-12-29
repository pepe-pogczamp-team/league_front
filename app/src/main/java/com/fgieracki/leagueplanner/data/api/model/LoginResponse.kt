package com.fgieracki.leagueplanner.data.api.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("id") val userId: Int
)
