package com.fgieracki.leagueplanner.data.api.model

import com.google.gson.annotations.SerializedName

data class TeamResponseDTO (@SerializedName("results") val teams: List<TeamResponse>)

data class TeamResponse(
    @SerializedName("id") val teamId: Int,
    @SerializedName("league") val leagueId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("city") val city: String,

)
