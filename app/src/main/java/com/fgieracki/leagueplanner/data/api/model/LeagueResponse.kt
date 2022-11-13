package com.fgieracki.leagueplanner.data.api.model

import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.Team
import com.google.gson.annotations.SerializedName

data class LeagueResponseDTO (@SerializedName("categories") val leagues: List<LeagueResponse>)

data class LeagueResponse (
    @SerializedName("strCategory") val name: String,
    @SerializedName("idCategory") val leagueId: String,
    @SerializedName("strCategoryDescription") val description: String,
    @SerializedName("strCategoryThumb") val imageUrl: String,
)