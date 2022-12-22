package com.fgieracki.leagueplanner.data.api.model

import com.google.gson.annotations.SerializedName

data class UpdateMatchDTO(
    @SerializedName("host_score") val hostScore: Int,
    @SerializedName("visitor_score") val visitorScore: Int
)
