package com.fgieracki.leagueplanner.data

import android.util.Log
import com.fgieracki.leagueplanner.data.api.LeagueWebService
import com.fgieracki.leagueplanner.data.mappers.toLeague
import com.fgieracki.leagueplanner.data.mappers.toLeagueList
import com.fgieracki.leagueplanner.data.mappers.toTeamList
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.Team
import kotlinx.coroutines.delay

val leagues = listOf<League>(
    League("test 1",1, 1,),
    League("test 2", 2, 2),
    League("test 3", 3, 3),
    League("test 4", 4, 4),
    League("test 5", 5, 5),
    League("test 6", 6, 6),
    League("test 7", 7, 1),
    League("test 8", 8, 8),
    League("test 9", 9, 9),
    League("test 10", 10, 10),
    League("test 11", 11, 11),
    League("test 12", 12, 12),
    League("test 13", 13, 1),
    League("test 14", 14, 14),
    League("test 15", 15, 1),
    League("test 16", 16, 16),
    League("test 17", 17, 17),
    League("test 18", 18, 18),
    League("test 19", 19, 19),
    League("test 20", 20, 20)
)

val teams = listOf<Team>(
    Team("team 1", 1, 1, 0, "Kraków", emptyList()),
    Team("team 2", 2, 1, 0, "Gdańsk", emptyList()),
    Team("team 3", 3, 1, 0, "Warszawa", emptyList()),
    Team("team 4", 4, 2, 0, "Poznań", emptyList()),
    Team("team 5", 5, 2, 0, "Wrocław", emptyList()),
    Team("team 6", 6, 2, 0, "Łódź", emptyList()),
    Team("team 7", 7, 3, 0, "Kraków", emptyList()),
    Team("team 8", 8, 3, 0, "Gdańsk", emptyList()),
)

val matches = listOf<Match>(
    Match(0, 1, 1, 2, 10, 23, "2020-01-01"),
    Match(1, 1, 3, 1, 10, 23, "2020-01-01"),
    Match(2, 1, 2, 3, 14, 12, "2020-01-01"),
    Match(3, 2, 4, 5, 10, 23, "2020-01-01"),
    Match(4, 2, 6, 4, 10, 23, "2020-01-01"),
    Match(5, 2, 5, 6, 14, 12, "2020-01-01"),
    Match(6, 3, 7, 8, 10, 23, "2020-01-01"),
)

class Repository(private val api: LeagueWebService = LeagueWebService) {
    suspend fun getLeagues(): List<League> {
//        val response = api.getLeagues()
//        Log.d("REPO_LEAGUES_RESP_CODE", response.code().toString())
//        return if(response.isSuccessful)
//            response.body()?.toLeagueList() ?: emptyList()
////            leagues
//        else
//            emptyList<League>() //TODO: handle errors


        return leagues.toList()
    }

    suspend fun getTeams(leagueId: Int): List<Team> {
//        val response = api.getTeams()
//        Log.d("REPO_TEAM_RESP_CODE", response.code().toString())
//        return if(response.isSuccessful)
////            response.body()?.toTeamList() ?: emptyList()
//            teams.filter {
//                it.leagueId == leagueId
//            }
//        else
//            emptyList<Team>() //TODO: handle errors


        return teams.filter {
            it.leagueId == leagueId
        }
    }

    suspend fun getLeague(leagueId: Int): League {
//        val response = api.getLeague(leagueId.toString())
//        Log.d("REPO_LEAGUE_RESP_CODE", response.code().toString())
//        return if(response.isSuccessful)
//            response.body()?.toLeague() ?: League()
////            leagues.first { it.leagueId == leagueId }
//        else
//            League() //TODO: handle errors


        return leagues.first { it.leagueId == leagueId }
    }

    suspend fun getMatches(leagueId: Int): List<Match> {
        //TODO: to implement

        return matches.filter {
            it.leagueId == leagueId
        }
    }

}