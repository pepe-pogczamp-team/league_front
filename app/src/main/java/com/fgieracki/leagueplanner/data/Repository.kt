package com.fgieracki.leagueplanner.data

import android.util.Log
import com.fgieracki.leagueplanner.data.api.LeagueWebService
import com.fgieracki.leagueplanner.data.api.model.AddLeagueDTO
import com.fgieracki.leagueplanner.data.api.model.AddMatchDTO
import com.fgieracki.leagueplanner.data.api.model.AddTeamDTO
import com.fgieracki.leagueplanner.data.api.model.UpdateMatchDTO
import com.fgieracki.leagueplanner.data.local.*
import com.fgieracki.leagueplanner.data.mappers.*
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.Team
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*


val token = "Token 3d3e0c9aea6bc496fa4430982022dd7b56a44a91"
val userId = 1


class Repository(
    private val api: LeagueWebService.LeaguePlannerApi = LeagueWebService.api,
    private val teamsCatcher: TeamsCatcher = InMemoryTeamsCatcher,
    private val matchesCatcher: MatchesCatcher = InMemoryMatchesCatcher,
    private val leaguesCatcher: LeaguesCatcher = InMemoryLeaguesCatcher
) {

    fun getLeagues(): Flow<List<League>> = flow<List<League>> {
        if (leaguesCatcher.getLeagues().first().isNotEmpty()) {
            emit(leaguesCatcher.getLeagues().first())
        }

        //TODO: add TRY CATCH
        val response = try {
            api.getLeagues(token)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        if (response?.isSuccessful == true) {
            val leagues = response.body()?.toLeagueList()
            leagues?.let {
                leaguesCatcher.clearLeagues()
                leaguesCatcher.addLeagues(it)
                emitAll(leaguesCatcher.getLeagues())
            }
        } else {
            emitAll(getLeagues())
        }
    }

    fun getTeams(leagueId: Int): Flow<List<Team>> = flow<List<Team>> {
        if (teamsCatcher.getTeams(leagueId).first().isNotEmpty()) {

            emit(teamsCatcher.getTeams(leagueId).first())
        }

        if (leagueId == -1) {
            emit(emptyList())
        } else {
            val response = api.getTeams(token, leagueId)
            if (response.isSuccessful) {
                val teams = response.body()?.toTeamList()
                teams?.let {
                    teamsCatcher.clearTeams()
                    teamsCatcher.addTeams(it)
                    emitAll(teamsCatcher.getTeams(leagueId))
                }
            } else {
                emitAll(getTeams(leagueId))
            }
        }
    }

    fun getLeague(leagueId: Int): Flow<League> {
        return leaguesCatcher.getLeague(leagueId)
    }

    fun getMatches(leagueId: Int): Flow<List<Match>> = flow<List<Match>> {
        if (matchesCatcher.getMatches(leagueId).first().isNotEmpty()) {
            emit(matchesCatcher.getMatches(leagueId).first())
        }
        if (leagueId == -1) {
            emit(emptyList())
        } else {
            val response = api.getMatches(token, leagueId)
            if (response.isSuccessful) {
                val matches = response.body()?.toMatchList()
                matches?.let {
                    matchesCatcher.clearMatches()
                    matchesCatcher.addMatches(it)
                    emitAll(matchesCatcher.getMatches(leagueId))
                }
            } else {
                emitAll(getMatches(leagueId))
            }
        }
    }

    suspend fun addLeague(newLeagueName: String): String {
        val response = api.addLeague(token, AddLeagueDTO(name = newLeagueName))
        if (response.isSuccessful) {
            val league = response.body()?.toLeague()
            league?.let {
                leaguesCatcher.addLeague(it)
            }
            return response.code().toString()
        }
//        Log.d("REPO_ADD_LEAGUE_RESP_CODE", response.code().toString())

        return response.code().toString()
    }

    suspend fun addTeam(newTeam: AddTeamDTO): String {
        //log AddTeamDTO values
//        Log.d("REPO_ADD_TEAM", newTeam.toString())


        val response = api.addTeam(token, newTeam)
        if (response.isSuccessful) {
            val team = response.body()?.toTeam()
            team?.let {
                teamsCatcher.addTeam(team)
            }
            return response.code().toString()
        }
//        Log.d("REPO_ADD_TEAM_RESP_CODE", response.code().toString())
        return response.code().toString()
    }

    suspend fun addMatch(newMatch: AddMatchDTO): String {
        val response = api.addMatch(token, newMatch)
        if(response.isSuccessful){
            val match = response.body()?.toMatch()
            match?.let {
                matchesCatcher.addMatch(match)
            }
            return response.code().toString()
        }
        return response.code().toString()
    }

    suspend fun updateMatch(matchId: Int, matchDetails: UpdateMatchDTO): String {
        val response = api.updateMatch(token, matchId, matchDetails)
        if(response.isSuccessful){
            val match = response.body()?.toMatch()
            match?.let {
                matchesCatcher.updateMatch(match)
            }
            return response.code().toString()
        }
        return response.code().toString()
    }
}
