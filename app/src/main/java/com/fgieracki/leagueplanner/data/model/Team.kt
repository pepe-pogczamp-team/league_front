package com.fgieracki.leagueplanner.data.model

import com.fgieracki.leagueplanner.data.model.Match

data class Team (
val name: String,
val teamId: Int,
val leagueId: Int,
val points: Int = 0,
val city: String = "Kraków", //TODO: remove
val matches: List<Match>? = emptyList()
)
