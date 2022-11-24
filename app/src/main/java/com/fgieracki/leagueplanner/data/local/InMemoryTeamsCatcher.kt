package com.fgieracki.leagueplanner.data.local

import android.util.Log
import com.fgieracki.leagueplanner.data.model.Team
import kotlinx.coroutines.flow.*

val ateams = listOf<Team>(
    Team("team 1", 1, 1, 12, "Kraków"),
    Team("team 2", 2, 1, 12, "Gdańsk"),
    Team("team 3", 3, 1, 43, "Warszawa"),
    Team("team 4", 4, 2, 55, "Poznań"),
    Team("team 5", 5, 2, 2, "Wrocław"),
    Team("team 6", 6, 2, 1, "Łódź"),
    Team("team 7", 7, 3, 44, "Kraków"),
    Team("team 8", 8, 3, 77, "Gdańsk"),
)


object InMemoryTeamsCatcher : TeamsCatcher {
    private val teams = MutableStateFlow<List<Team>>(listOf())

    override fun getTeams(leagueId: Int): Flow<List<Team>> {
        return teams.map {
            it.filter { it.leagueId == leagueId }
        }
    }

    override fun addTeam(team: Team) {
        teams.update { it + team }
    }

    override fun deleteTeam(team: Team) {
        teams.update { it - team }
    }

    override fun updateTeam(team: Team) {
        teams.update { it.map { if (it.teamId == team.teamId) team else it } }
    }

    override fun getTeam(teamId: Int): Flow<Team> {
        return teams.map { it.first { it.teamId == teamId } }
    }

    override fun addTeams(teams: List<Team>) {
        this.teams.update { it + teams }
    }

    override fun clearTeams() {
        this.teams.update { emptyList() }
    }
}