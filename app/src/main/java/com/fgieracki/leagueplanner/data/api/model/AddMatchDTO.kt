package com.fgieracki.leagueplanner.data.api.model

import com.google.gson.annotations.SerializedName

data class AddMatchDTO (
    @SerializedName("league") val league: Int,
    @SerializedName("host") val homeTeamId: Int,
    @SerializedName("host_score") val homeTeamScore: Int,
    @SerializedName("visitor") val awayTeamId: Int,
    @SerializedName("visitor_score") val awayTeamScore: Int,
    @SerializedName("datetime") val date: String,
    @SerializedName("address") val location: String,
    @SerializedName("city") val city: String
)