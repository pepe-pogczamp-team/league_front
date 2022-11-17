package com.fgieracki.leagueplanner.data.local

import com.fgieracki.leagueplanner.data.model.Match
import kotlinx.coroutines.flow.Flow

interface MatchesCatcher {
    fun getMatches(leagueId: Int): Flow<List<Match>>
    fun addMatch(match: Match)
    fun deleteMatch(match: Match)
    fun updateMatch(match: Match)
    fun getMatch(matchId: Int): Flow<Match>
}