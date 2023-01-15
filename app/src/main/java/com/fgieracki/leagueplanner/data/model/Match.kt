package com.fgieracki.leagueplanner.data.model

data class Match (
    val matchId: Int,
    val leagueId: Int,
    val homeTeamId: Int,
    val awayTeamId: Int,
    val homeTeamScore: Int?,
    val awayTeamScore: Int?,
    val matchDate: String,
    val matchLocation: String? = null,
    val goodWeather: Boolean? = null
)
