package com.fgieracki.leagueplanner.data

class League (
    val name: String,
    val leagueId: Int,
    val ownerId: Int,
    val teams: List<Team>? = null,
    val matches: List<Match>? = null
)
