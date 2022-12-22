package com.fgieracki.leagueplanner.data.model

data class MatchDisplay(
    val matchId: Int,
    val leagueId: Int,
    val homeTeamId: Int,
    val awayTeamId: Int,
    val homeTeamScore: Int?,
    val awayTeamScore: Int?,
    val matchDate: String,
    val matchLocation: String?,
    val homeTeamName: String,
    val awayTeamName: String,
    val homeTeamCity: String,
    val awayTeamCity: String,
)

