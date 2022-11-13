package com.fgieracki.leagueplanner.data.model

import com.google.gson.annotations.SerializedName

data class League (
    val name: String,
    val leagueId: Int,
    val ownerId: Int,
    val teams: List<Team>? = null,
    val matches: List<Match>? = null
)
