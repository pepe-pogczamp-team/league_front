package com.fgieracki.leagueplanner.data.api.model

import com.google.gson.annotations.SerializedName

data class LeagueResponseDTO (@SerializedName("results") val leagues: List<LeagueResponse>)

data class LeagueResponse (
    @SerializedName("id") val leagueId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("owner") val ownerId: Int
)