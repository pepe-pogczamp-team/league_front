package com.fgieracki.leagueplanner.data.model

import com.google.gson.annotations.SerializedName

data class League (
    val name: String = "",
    val leagueId: Int = 0,
    val ownerId: Int = -1,
)
