package com.fgieracki.leagueplanner.data.local

import com.fgieracki.leagueplanner.data.model.League
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update



object InMemoryLeaguesCatcher : LeaguesCatcher {
    private val leagues = MutableStateFlow<List<League>>(emptyList())

    override fun getLeagues(): Flow<List<League>> {
        return leagues
    }

    override fun addLeague(league: League) {
        leagues.update { leagues ->
            leagues + league
        }
    }

    override fun deleteLeague(league: League) {
        leagues.update { leagues ->
            leagues - league
        }
    }

    override fun updateLeague(league: League) {
        leagues.update { leagues ->
            leagues.map {
                if (it.leagueId == league.leagueId) league else it
            }
        }
    }

    override fun getLeague(leagueId: Int): Flow<League> {
        return leagues.map { leagues ->
            leagues.find { it.leagueId == leagueId } ?: League("", 0, 0)
        }
    }

    override fun addLeagues(leagues: List<League>) {
        this.leagues.update { leagues }
    }

    override fun clearLeagues() {
        leagues.update { emptyList() }
    }
}
