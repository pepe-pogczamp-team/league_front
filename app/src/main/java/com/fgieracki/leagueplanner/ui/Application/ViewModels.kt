package com.fgieracki.leagueplanner.ui.Application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fgieracki.leagueplanner.data.Repository
import com.fgieracki.leagueplanner.data.api.model.AddMatchDTO
import com.fgieracki.leagueplanner.data.api.model.AddTeamDTO
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

const val globalUserId: Int = 1

class LeagueListViewModel(private val repository: Repository = Repository()) : ViewModel() {
    //TODO: handle not full form filled
    val leaguesState: Flow<List<League>> = repository.getLeagues()
    val newLeagueName: MutableStateFlow<String> = MutableStateFlow("")
    val userId = globalUserId
    fun onLeagueNameChange(newName: String) {
        newLeagueName.value = newName
    }

    fun addLeague(): Boolean {
        if(newLeagueName.value == "") {return false}
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLeague(newLeagueName.value)
            newLeagueName.value = ""
        }
        return true;
    }
}


class TeamsViewModel(private val repository: Repository = Repository()) : ViewModel() {
    //TODO: handle not full form filled
    var teamsState: Flow<List<Team>> = repository.getTeams(-1)
    var matchesState: Flow<List<Match>> = repository.getMatches(-1)
    var leagueState: Flow<League> = repository.getLeague(-1)
    val userId = globalUserId
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

class MatchesViewModel(private val repository: Repository = Repository()) : ViewModel() {

    var teamsState: Flow<List<Team>> = repository.getTeams(-1)
    var matchesState: Flow<List<Match>> = repository.getMatches(-1)
    var leagueState: Flow<League> = repository.getLeague(-1)
    val userId = globalUserId

    val newHost: MutableStateFlow<Team> = MutableStateFlow(
        Team("", -1, -1, 0))
    val newGuest: MutableStateFlow<Team> = MutableStateFlow(
        Team("", -1, -1, 0))
    val newHostScore: MutableStateFlow<Int> = MutableStateFlow(0)
    val newGuestScore: MutableStateFlow<Int> = MutableStateFlow(0)
    val newAddress: MutableStateFlow<String> = MutableStateFlow("")

    //TODO: handle Datetime

    fun onHostChange(newHost: Team) {
        this.newHost.value = newHost
    }

    fun onGuestChange(newGuest: Team) {
        this.newGuest.value = newGuest
    }

    fun onHostScoreChange(newHostScore: Int) {
        this.newHostScore.value = newHostScore
    }

    fun onGuestScoreChange(newGuestScore: Int) {
        this.newGuestScore.value = newGuestScore
    }

    fun onLocationChange(newLocation: String) {
        this.newAddress.value = newLocation
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

    fun addMatch() {
        //TODO: handle not full form filled

        viewModelScope.launch(Dispatchers.IO) {
            val leagueId = leagueState.first().leagueId
            repository.addMatch(
                AddMatchDTO(
                    league = leagueId,
                    homeTeamId = newHost.value.teamId,
                    homeTeamScore = newHostScore.value,
                    awayTeamId = newGuest.value.teamId,
                    awayTeamScore = newGuestScore.value,
                    date = "", //TODO: FIX ME!
                    location = newAddress.value,
                    city = newHost.value.city
                )
            )
            newAddress.value = ""
            newAddress.value = ""
            newHostScore.value = 0
            newGuestScore.value = 0
            newHost.value = Team("", -1, -1, 0)
            newGuest.value = Team("", -1, -1, 0)
            //TODO: clear the date
        }
    }

    fun refreshMatches(leagueId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            matchesState = repository.getMatches(leagueId)
        }
    }
}