package com.fgieracki.leagueplanner.data.mappers

import com.fgieracki.leagueplanner.data.api.model.LeagueResponse
import com.fgieracki.leagueplanner.data.api.model.LeagueResponseDTO
import com.fgieracki.leagueplanner.data.api.model.TeamResponseDTO
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.MatchDisplay
import com.fgieracki.leagueplanner.data.model.Team

fun LeagueResponseDTO.toLeagueList(): List<League> {
    return leagues.map {
        League(
            name = it.name,
            leagueId = it.leagueId.toInt(),
            ownerId = it.leagueId.toInt(),
        )
    }
}

fun TeamResponseDTO.toTeamList(): List<Team> {
    return teams.map {
        Team(
            name = it.name,
            teamId = it.teamId.toInt(),
            leagueId = 1, //TODO: fix this
            points = 0,
            city = "Kraków", //TODO: fix this
        )
    }
}

fun LeagueResponse.toLeague(): League {
    return League(
        name = name,
        leagueId = leagueId.toInt(),
        ownerId = leagueId.toInt(),
    )
}

fun mapMatchAndTeamToMatchDisplay(match: Match, teams: List<Team>): MatchDisplay {
//    homeTeam: Team, awayTeam: Team
    val homeTeam = teams.find { it.teamId == match.homeTeamId }
    val awayTeam = teams.find { it.teamId == match.awayTeamId }
    return MatchDisplay(
        matchId = match.matchId,
        leagueId = match.leagueId,
        homeTeamId = match.homeTeamId,
        awayTeamId = match.awayTeamId,
        homeTeamScore = match.homeTeamScore,
        awayTeamScore = match.awayTeamScore,
        matchDate = match.matchDate,
        homeTeamName = homeTeam?.name.orEmpty(),
        awayTeamName = awayTeam?.name.orEmpty(),
        homeTeamCity = homeTeam?.city.orEmpty(),
        awayTeamCity = awayTeam?.city.orEmpty(),
    )
}