package com.fgieracki.leagueplanner.data.api.model

import com.google.gson.annotations.SerializedName

data class MatchResponseDTO (@SerializedName("results") val matches: List<MatchResponse>)

data class MatchResponse(
    @SerializedName("id") val matchId: Int,
    @SerializedName("league") val leagueId: Int,
    @SerializedName("host") val homeTeamId: Int,
    @SerializedName("host_score") val homeTeamScore: Int,
    @SerializedName("visitor") val awayTeamId: Int,
    @SerializedName("visitor_score") val awayTeamScore: Int,
    @SerializedName("address") val address: String,
    @SerializedName("datetime") val date: String,
)