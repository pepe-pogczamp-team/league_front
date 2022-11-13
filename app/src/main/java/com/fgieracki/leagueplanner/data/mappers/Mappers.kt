package com.fgieracki.leagueplanner.data.mappers

import com.fgieracki.leagueplanner.data.api.model.LeagueResponse
import com.fgieracki.leagueplanner.data.api.model.LeagueResponseDTO
import com.fgieracki.leagueplanner.data.api.model.TeamResponseDTO
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Team

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

fun TeamResponseDTO.toTeamList(): List<Team> {
    return teams.map {
        Team(
            name = it.name,
            teamId = it.teamId.toInt(),
//            leagueId = it.teamId.toInt(),
            leagueId = 1,
            points = 0,
            city = "Kraków",
            matches = emptyList()
        )
    }
}

fun LeagueResponse.toLeague(): League {
    return League(
        name = name,
        leagueId = leagueId.toInt(),
        ownerId = leagueId.toInt(),
        teams = emptyList(),
        matches = emptyList()
    )
}