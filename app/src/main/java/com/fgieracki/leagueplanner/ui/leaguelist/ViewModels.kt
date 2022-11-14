package com.fgieracki.leagueplanner.ui.leaguelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fgieracki.leagueplanner.data.Repository
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LeagueListViewModel(private val repository: Repository = Repository()) : ViewModel() {
    val leaguesState: MutableStateFlow<List<League>> = MutableStateFlow(emptyList<League>())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val leagues = getLeagues()
            leaguesState.value = leagues
        }
    }

    private suspend fun getLeagues(): List<League> {
        return repository.getLeagues()
    }

}


class TeamsAndMatchesViewModel(private val repository: Repository = Repository()) : ViewModel() {

    val teamsState: MutableStateFlow<List<Team>> = MutableStateFlow(emptyList<Team>())
    val leagueState: MutableStateFlow<League> = MutableStateFlow(League())
    val matchesState: MutableStateFlow<List<Match>> = MutableStateFlow(emptyList<Match>())

    fun refreshTeams(leagueId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val teams = repository.getTeams(leagueId)
            teamsState.value = teams
        }
    }

    fun refreshLeague(leagueId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val league = repository.getLeague(leagueId)
            leagueState.value = league
        }
    }

    fun refreshMatches(leagueId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val matches = repository.getMatches(leagueId)
            matchesState.value = matches
        }
    }
}
