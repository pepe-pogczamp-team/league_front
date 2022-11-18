package com.fgieracki.leagueplanner.data

import android.util.Log
import com.fgieracki.leagueplanner.data.api.LeagueWebService
import com.fgieracki.leagueplanner.data.api.model.AddLeagueDTO
import com.fgieracki.leagueplanner.data.api.model.AddTeamDTO
import com.fgieracki.leagueplanner.data.local.*
import com.fgieracki.leagueplanner.data.mappers.toLeague
import com.fgieracki.leagueplanner.data.mappers.toLeagueList
import com.fgieracki.leagueplanner.data.mappers.toTeam
import com.fgieracki.leagueplanner.data.mappers.toTeamList
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.Team
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*


val token = "Token 3d3e0c9aea6bc496fa4430982022dd7b56a44a91"
val userId = 1


class Repository(private val api: LeagueWebService.LeaguePlannerApi = LeagueWebService.api,
                 private val teamsCatcher: TeamsCatcher = InMemoryTeamsCatcher,
                 private val matchesCatcher: MatchesCatcher = InMemoryMatchesCatcher,
                 private val leaguesCatcher: LeaguesCatcher = InMemoryLeaguesCatcher
) {

    fun getLeagues(): Flow<List<League>> = flow<List<League>> {
        if(leaguesCatcher.getLeagues().first().isNotEmpty()){
            emit(leaguesCatcher.getLeagues().first())
        }

        val response = api.getLeagues(token)
        if (response.isSuccessful) {
            val leagues = response.body()?.toLeagueList()
            leagues?.let {
                leaguesCatcher.clearLeagues()
                leaguesCatcher.addLeagues(it)
                emitAll(leaguesCatcher.getLeagues())
            }
        } else{
            emitAll(getLeagues())
        }
    }

    fun getTeams(leagueId: Int): Flow<List<Team>> = flow<List<Team>> {
        if(teamsCatcher.getTeams(leagueId).first().isNotEmpty()){
            emit(teamsCatcher.getTeams(leagueId).first())
        }

        val response = api.getTeams(token, leagueId)
        if (response.isSuccessful) {
            val teams = response.body()?.toTeamList()
            teams?.let {
                teamsCatcher.clearTeams()
                teamsCatcher.addTeams(it)
                emitAll(teamsCatcher.getTeams(leagueId))
            }
        } else{
            emitAll(getTeams(leagueId))
        }
    }

    fun getLeague(leagueId: Int): Flow<League> {
            return leaguesCatcher.getLeague(leagueId)
    }

    fun getMatches(leagueId: Int): Flow<List<Match>> {
        //TODO: to implement

//        return matchesState.map {
//            it.filter {
//                it.leagueId == leagueId
//            }
//        }

//        return matches.filter {
//            it.leagueId == leagueId
//        }
        return matchesCatcher.getMatches(leagueId)
    }

    suspend fun addLeague(newLeagueName: String): Boolean{
        val response = api.addLeague(token, AddLeagueDTO(name = newLeagueName))
        if(response.isSuccessful){
            val league = response.body()?.toLeague()
            league?.let {
                leaguesCatcher.addLeague(it)
            }
            return true
        }
        Log.d("REPO_ADD_LEAGUE_RESP_CODE", response.code().toString())

        return response.isSuccessful
    }

    suspend fun addTeam(newTeam: AddTeamDTO): Boolean{
        //log AddTeamDTO values
        Log.d("REPO_ADD_TEAM", newTeam.toString())


        val response = api.addTeam(token, newTeam)
        if(response.isSuccessful){
            val team = response.body()?.toTeam()
            team?.let {
                teamsCatcher.addTeam(team)
            }
            return true
        }
        Log.d("REPO_ADD_TEAM_RESP_CODE", response.code().toString())
        return response.isSuccessful
    }
}
