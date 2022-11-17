package com.fgieracki.leagueplanner.data.local

import com.fgieracki.leagueplanner.data.model.Team
import kotlinx.coroutines.flow.Flow

interface TeamsCatcher {
    fun getTeams(leagueId: Int): Flow<List<Team>>
    fun addTeam(team: Team)
    fun deleteTeam(team: Team)
    fun updateTeam(team: Team)
    fun getTeam(teamId: Int): Flow<Team>
    fun addTeams(teams: List<Team>)
    fun clearTeams()


}