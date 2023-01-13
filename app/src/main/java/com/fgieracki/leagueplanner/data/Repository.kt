package com.fgieracki.leagueplanner.data

import android.content.Context
import com.fgieracki.leagueplanner.data.api.LeagueWebService
import com.fgieracki.leagueplanner.data.api.model.*
import com.fgieracki.leagueplanner.data.local.*
import com.fgieracki.leagueplanner.data.mappers.*
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.Team
import kotlinx.coroutines.flow.*
import retrofit2.Response

class Repository(
    private val api: LeagueWebService.LeaguePlannerApi = LeagueWebService.api,
    private val teamsCatcher: TeamsCatcher = InMemoryTeamsCatcher,
    private val matchesCatcher: MatchesCatcher = InMemoryMatchesCatcher,
    private val leaguesCatcher: LeaguesCatcher = InMemoryLeaguesCatcher
) {
    private var USER_TOKEN = "Token"
    private val sharedPreference =  ContextCatcher.getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)

    private fun getToken() {
        val token: String = sharedPreference.getString("USER_TOKEN", "Token")?:"Token"
        USER_TOKEN = token
    }

    fun getLeagues(): Flow<List<League>> = flow<List<League>> {
        getToken()

        if (leaguesCatcher.getLeagues().first().isNotEmpty()) {
            emit(leaguesCatcher.getLeagues().first())
        }

        val response = try {
            api.getLeagues(USER_TOKEN)
        } catch (e: Exception) {
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
        getToken()
        if (teamsCatcher.getTeams(leagueId).first().isNotEmpty()) {

            emit(teamsCatcher.getTeams(leagueId).first())
        }

        if (leagueId == -1) {
            emit(emptyList())
        } else {
            val response = api.getTeams(USER_TOKEN, leagueId)
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
        getToken()
        if (matchesCatcher.getMatches(leagueId).first().isNotEmpty()) {
            emit(matchesCatcher.getMatches(leagueId).first())
        }
        if (leagueId == -1) {
            emit(emptyList())
        } else {
            val response = api.getMatches(USER_TOKEN, leagueId)
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
        getToken()
        val response = api.addLeague(USER_TOKEN, AddLeagueDTO(name = newLeagueName))
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


        val response = api.addTeam(USER_TOKEN, newTeam)
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
        val response = api.addMatch(USER_TOKEN, newMatch)
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
        val response = api.updateMatch(USER_TOKEN, matchId, matchDetails)
        if(response.isSuccessful){
            val match = response.body()?.toMatch()
            match?.let {
                matchesCatcher.updateMatch(match)
            }
            return response.code().toString()
        }
        return response.code().toString()
    }

    suspend fun signIn(username: String, password: String): Response<LoginResponse> {
        val response = api.signIn(LoginData(username, password))
        if(response.isSuccessful){
            val token = response.body()?.token
            token?.let {
                USER_TOKEN = token
            }
        }
        return response
//        return api.signIn(LoginData(username, password))
    }

    suspend fun signUp(username: String, password: String): Response<RegisterResponse> {
        return api.signUp(LoginData(username, password))
    }
}
