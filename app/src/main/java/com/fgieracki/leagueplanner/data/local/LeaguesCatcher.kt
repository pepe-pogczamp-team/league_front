package com.fgieracki.leagueplanner.data.local

import com.fgieracki.leagueplanner.data.model.League
import kotlinx.coroutines.flow.Flow

interface LeaguesCatcher {
    fun getLeagues(): Flow<List<League>>
    fun addLeague(league: League)
    fun deleteLeague(league: League)
    fun updateLeague(league: League)
    fun getLeague(leagueId: Int): Flow<League>
    fun addLeagues(leagues: List<League>)
    fun clearLeagues()

}