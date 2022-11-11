package com.fgieracki.leagueplanner.data

class Match (
    val matchId: Int,
    val leagueId: Int,
    val homeTeamId: Int,
    val awayTeamId: Int,
    val homeTeamScore: Int?,
    val awayTeamScore: Int?,
    val matchDate: String,
    val matchPlace: String
        ){

}
