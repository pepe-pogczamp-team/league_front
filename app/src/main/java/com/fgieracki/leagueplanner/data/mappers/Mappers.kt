package com.fgieracki.leagueplanner.data.mappers

import com.fgieracki.leagueplanner.data.api.model.*
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.MatchDisplay
import com.fgieracki.leagueplanner.data.model.Team

fun LeagueResponseDTO.toLeagueList(): List<League> {
    return leagues.map {
        it.toLeague()
    }
}

fun TeamResponseDTO.toTeamList(): List<Team> {
    return teams.map {
        it.toTeam()
    }
}

fun MatchResponseDTO.toMatchList(): List<Match> {
    return matches.map {
        it.toMatch()
    }
}

fun LeagueResponse.toLeague(): League {
    return League(
        name = name,
        leagueId = leagueId,
        ownerId = ownerId,
    )
}

fun TeamResponse.toTeam(): Team {
    return Team(
        name = name,
        teamId = teamId,
        leagueId = leagueId,
        points = score,
        city = city,
    )
}

fun MatchResponse.toMatch(): Match {
    return Match(
        matchId = matchId,
        homeTeamId = homeTeamId,
        awayTeamId = awayTeamId,
        homeTeamScore = homeTeamScore,
        awayTeamScore = awayTeamScore,
        leagueId = leagueId,
        matchDate = date,
        matchLocation = address,
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
        matchLocation = match.matchLocation,
        homeTeamName = homeTeam?.name.orEmpty(),
        awayTeamName = awayTeam?.name.orEmpty(),
        homeTeamCity = homeTeam?.city.orEmpty(),
        awayTeamCity = awayTeam?.city.orEmpty(),
    )
}