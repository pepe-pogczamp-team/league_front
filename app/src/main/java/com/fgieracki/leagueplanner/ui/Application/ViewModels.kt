package com.fgieracki.leagueplanner.ui.Application

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

const val globalUserId: Int = 1

class LeagueListViewModel(private val repository: Repository = Repository()) : ViewModel() {
    val leaguesState: Flow<List<League>> = repository.getLeagues()
    val newLeagueName: MutableStateFlow<String> = MutableStateFlow("")
    val toastChannel = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val userId = globalUserId
    fun onLeagueNameChange(newName: String) {
        newLeagueName.value = newName
    }

    fun addLeague(): Boolean {

        if (newLeagueName.value == "") {
            return false
        }

        viewModelScope.launch() {
            val response: String = repository.addLeague(newLeagueName.value)
            if (response == "201") toastChannel.emit("Dodano ligę!")
            else toastChannel.emit("Wystąpił błąd! Spróbuj ponownie!")
            newLeagueName.value = ""
        }
        return true
    }

}


class TeamsViewModel(private val repository: Repository = Repository()) : ViewModel() {
    //TODO: handle not full form filled
    var teamsState: Flow<List<Team>> = repository.getTeams(-1)
    var leagueState: Flow<League> = repository.getLeague(-1)
    val userId = globalUserId
    val newTeamName: MutableStateFlow<String> = MutableStateFlow("")
    val newTeamCity: MutableStateFlow<String> = MutableStateFlow("")
    val toastChannel = MutableSharedFlow<String>(extraBufferCapacity = 1)

    fun onTeamNameChange(newName: String) {
        newTeamName.value = newName
    }

    fun onTeamCityChange(newCity: String) {
        newTeamCity.value = newCity
    }

    fun addTeam(): Boolean {
        if (newTeamName.value == ""
            || newTeamCity.value == ""
        ) return false

        viewModelScope.launch() {
            val leagueId = leagueState.first().leagueId
            val response = repository.addTeam(
                AddTeamDTO(
                    name = newTeamName.value,
                    city = newTeamCity.value,
                    league = leagueId
                )
            )

            if (response == "201") toastChannel.emit("Dodano drużynę!")
            else toastChannel.emit("Wystąpił błąd! Spróbuj ponownie!")

            newTeamName.value = ""
            newTeamCity.value = ""
        }
        return true
    }

    fun refreshTeams(leagueId: Int) {

        viewModelScope.launch() {
            teamsState = repository.getTeams(leagueId)
        }
    }

    fun refreshLeague(leagueId: Int) {
        viewModelScope.launch() {
            val league = repository.getLeague(leagueId)
            league.collect {
                leagueState = repository.getLeague(leagueId)
            }
        }
    }
}

class MatchesViewModel(private val repository: Repository = Repository()) : ViewModel() {

    var teamsState: Flow<List<Team>> = repository.getTeams(-1)
    var matchesState: Flow<List<Match>> = repository.getMatches(-1)
    var leagueState: Flow<League> = repository.getLeague(-1)
    val userId = globalUserId

    val newHost: MutableStateFlow<Team> = MutableStateFlow(
        Team("", -1, -1, 0)
    )
    val newGuest: MutableStateFlow<Team> = MutableStateFlow(
        Team("", -1, -1, 0)
    )
    val newHostScore: MutableStateFlow<String> = MutableStateFlow("")
    val newGuestScore: MutableStateFlow<String> = MutableStateFlow("")
    val newAddress: MutableStateFlow<String> = MutableStateFlow("")
    val newDate: MutableStateFlow<String> = MutableStateFlow("")
    val newTime: MutableStateFlow<String> = MutableStateFlow("")

    val toastChannel = MutableSharedFlow<String>(extraBufferCapacity = 1)

    //TODO: handle Datetime

    fun onHostChange(newHost: Team): Boolean {
        if (newHost.teamId == newGuest.value.teamId) return false
        this.newHost.value = newHost
        return true
    }

    fun onGuestChange(newGuest: Team): Boolean {
        if (newGuest.teamId == newHost.value.teamId) return false
        this.newGuest.value = newGuest
        return true
    }

    fun onHostScoreChange(newHostScore: String): Boolean {
        if (newHostScore == "" || newHostScore.toIntOrNull() != null) {
            this.newHostScore.value = newHostScore
            return true
        }
        return false
    }

    fun onGuestScoreChange(newGuestScore: String): Boolean {
        if (newGuestScore == "" || newGuestScore.toIntOrNull() != null) {
            this.newGuestScore.value = newGuestScore
            return true
        }
        return false
    }

    fun onLocationChange(newLocation: String) {
        this.newAddress.value = newLocation
    }

    fun onDateChange(newDate: String) {
        this.newDate.value = newDate
    }

    fun onTimeChange(newTime: String) {
        this.newTime.value = newTime
    }

    fun refreshTeams(leagueId: Int) {

        viewModelScope.launch() {
            teamsState = repository.getTeams(leagueId)
        }
    }

    fun refreshLeague(leagueId: Int) {
        viewModelScope.launch() {
            val league = repository.getLeague(leagueId)
            league.collect {
                leagueState = repository.getLeague(leagueId)
            }
        }
    }

    fun clearForm() {
        newHost.value = Team("", -1, -1, 0)
        newGuest.value = Team("", -1, -1, 0)
        newHostScore.value = ""
        newGuestScore.value = ""
        newAddress.value = ""
    }

    fun addMatch(): Boolean {
        //TODO: handle not full form filled
        if (newHost.value.teamId == -1
            || newGuest.value.teamId == -1
            || newAddress.value == ""
        ) return false
        viewModelScope.launch() {
            val leagueId = leagueState.first().leagueId
            val response = repository.addMatch( //TODO: make method to verify data
                AddMatchDTO(
                    league = leagueId,
                    homeTeamId = newHost.value.teamId,
                    homeTeamScore = newHostScore.value.toIntOrNull() ?: 0,
                    awayTeamId = newGuest.value.teamId,
                    awayTeamScore = newGuestScore.value.toIntOrNull() ?: 0,
                    date = "2022-11-15T13:45:00", //TODO: FIX ME!
                    location = newAddress.value,
                    city = newHost.value.city
                )
            )
            if (response == "201") toastChannel.emit("Dodano mecz!")
            else toastChannel.emit("Wystąpił błąd! Spróbuj ponownie!")
            clearForm()
        }
        return true
    }

    fun refreshMatches(leagueId: Int) {
        viewModelScope.launch() {
            matchesState = repository.getMatches(leagueId)
        }
    }
}