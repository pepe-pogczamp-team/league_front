package com.fgieracki.leagueplanner.ui.Application

import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fgieracki.leagueplanner.data.Repository
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LeagueListViewModel(private val repository: Repository = Repository()) : ViewModel() {
    val leaguesState: Flow<List<League>> = repository.getLeagues()
    val newLeagueName: MutableStateFlow<String> = MutableStateFlow("")

    fun onLeagueNameChanged(newName: String) {
        newLeagueName.value = newName
    }

    fun addLeague() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLeague(newLeagueName.value)
            newLeagueName.value = ""
        }
    }
}


class TeamsAndMatchesViewModel(private val repository: Repository = Repository()) : ViewModel() {

    var teamsState: Flow<List<Team>> = repository.getTeams(-1)
    var matchesState: Flow<List<Match>> = repository.getMatches(-1)
    var leagueState: Flow<League> = repository.getLeague(-1)

    fun refreshTeams(leagueId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            teamsState = repository.getTeams(leagueId)
        }
    }

    fun refreshLeague(leagueId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val league = repository.getLeague(leagueId)
            league.collect{
                leagueState = repository.getLeague(leagueId)
            }
        }
    }

    fun refreshMatches(leagueId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            matchesState = repository.getMatches(leagueId)
        }
    }
}
