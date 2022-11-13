package com.fgieracki.leagueplanner.data.mappers

import com.fgieracki.leagueplanner.data.api.model.LeagueResponse
import com.fgieracki.leagueplanner.data.api.model.LeagueResponseDTO
import com.fgieracki.leagueplanner.data.model.League

fun LeagueResponseDTO.toLeagueList(): List<League> {
    return leagues.map {
        League(
            name = it.name,
            leagueId = it.leagueId.toInt(),
            ownerId = it.leagueId.toInt(),
            teams = emptyList(),
            matches = emptyList()
        )
    }
}