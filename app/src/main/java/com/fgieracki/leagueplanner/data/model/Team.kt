package com.fgieracki.leagueplanner.data.model

import com.fgieracki.leagueplanner.data.model.Match

class Team (
    val name: String,
    val teamId: Int,
    val leagueId: Int,
    val points: Int,
    val matches: List<Match>
        ){

}
