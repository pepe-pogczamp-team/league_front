package com.fgieracki.leagueplanner.data.api.model

import com.google.gson.annotations.SerializedName

data class TeamResponseDTO (@SerializedName("meals") val teams: List<TeamResponse>)

data class TeamResponse(
    @SerializedName("idIngredient") val teamId: String,
    @SerializedName("strIngredient") val name: String,
)
