package com.fgieracki.leagueplanner.data.api.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
)
