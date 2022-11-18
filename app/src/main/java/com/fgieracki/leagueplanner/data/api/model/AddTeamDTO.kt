package com.fgieracki.leagueplanner.data.api.model

import com.google.gson.annotations.SerializedName

data class AddTeamDTO (
   @SerializedName("league") val league: Int,
   @SerializedName("name") val name: String,
   @SerializedName("city") val city: String,
)