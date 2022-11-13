package com.fgieracki.leagueplanner.data.api.model

import com.google.gson.annotations.SerializedName

data class LeagueResponseDTO (@SerializedName("categories") val leagues: List<LeagueResponse>)

data class LeagueResponse (
    @SerializedName("strCategory") val name: String,
    @SerializedName("idCategory") val leagueId: String,
)