package com.fgieracki.leagueplanner.data.local

import com.fgieracki.leagueplanner.data.model.Match
import kotlinx.coroutines.flow.*

//val amatches = listOf<Match>(
//    Match(0, 1, 1, 2, 10, 23, "2020-01-01"),
//    Match(1, 1, 3, 1, 10, 23, "2020-01-01"),
//    Match(2, 1, 2, 3, 14, 12, "2020-01-01"),
//    Match(3, 2, 4, 5, 10, 23, "2020-01-01"),
//    Match(4, 2, 6, 4, 10, 23, "2020-01-01"),
//    Match(5, 2, 5, 6, 14, 12, "2020-01-01"),
//    Match(6, 3, 7, 8, 10, 23, "2020-01-01"),
//)

object InMemoryMatchesCatcher : MatchesCatcher {
    private val matches = MutableStateFlow<List<Match>>(emptyList())

    override fun getMatches(leagueId: Int): Flow<List<Match>> {
        return matches.map { it.filter { it.leagueId == leagueId } }
    }

    override fun addMatch(match: Match) {
        matches.update { it + match }
    }

    override fun deleteMatch(match: Match) {
        matches.update { it - match }
    }

    override fun updateMatch(match: Match) {
        matches.update { it.map { if (it.matchId == match.matchId) match else it } }
    }

    override fun getMatch(matchId: Int): Flow<Match> {
        return matches.map { it.first { it.matchId == matchId } }
    }

    override fun addMatches(matches: List<Match>) {
        this.matches.update { it + matches }
    }

    override fun clearMatches() {
        this.matches.update { emptyList() }
    }


}