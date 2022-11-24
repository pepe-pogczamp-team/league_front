package com.fgieracki.leagueplanner.ui.Application

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fgieracki.leagueplanner.data.Repository
import com.fgieracki.leagueplanner.data.api.model.AddTeamDTO
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LeagueListViewModel(private val repository: Repository = Repository()) : ViewModel() {
    val leaguesState: Flow<List<League>> = repository.getLeagues()
    val newLeagueName: MutableStateFlow<String> = MutableStateFlow("")

    fun onLeagueNameChange(newName: String) {
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

    val newTeamName: MutableStateFlow<String> = MutableStateFlow("")
    val newTeamCity: MutableStateFlow<String> = MutableStateFlow("")

    fun onTeamNameChange(newName: String) {
        newTeamName.value = newName
    }

    fun onTeamCityChange(newCity: String) {
        newTeamCity.value = newCity
    }

    fun addTeam() {
        viewModelScope.launch(Dispatchers.IO) {
            val leagueId = leagueState.first().leagueId
            repository.addTeam(
                AddTeamDTO(
                    name = newTeamName.value,
                    city = newTeamCity.value,
                    league = leagueId
                )
            )
            newTeamName.value = ""
            newTeamCity.value = ""
        }
    }

    fun refreshTeams(leagueId: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            teamsState = repository.getTeams(leagueId)
        }
    }

    fun refreshLeague(leagueId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val league = repository.getLeague(leagueId)
            league.collect {
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
