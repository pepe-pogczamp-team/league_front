package com.fgieracki.leagueplanner.ui.Application

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fgieracki.leagueplanner.data.Repository
import com.fgieracki.leagueplanner.data.api.model.AddMatchDTO
import com.fgieracki.leagueplanner.data.api.model.AddTeamDTO
import com.fgieracki.leagueplanner.data.api.model.UpdateMatchDTO
import com.fgieracki.leagueplanner.data.local.ContextCatcher
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.MatchDisplay
import com.fgieracki.leagueplanner.data.model.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

var globalUserId = -1

class SignInViewModel(private val repository: Repository = Repository()) : ViewModel() {

    private val _toastChannel = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val toastChannel = _toastChannel

    val navChannel = MutableSharedFlow<String>(extraBufferCapacity = 1)


    val userId = MutableStateFlow<Int>(-1)

    val userToken = MutableStateFlow<String>("Token")

    val username: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")


    fun signIn() {
        viewModelScope.launch(Dispatchers.IO) {
            if (validateData()) {
                val response = repository.signIn(username.value, password.value)
                if (response.isSuccessful) {
                    userId.value = response.body()!!.userId
                    userToken.value = "Token " + response.body()!!.token
                    globalUserId = userId.value

                    val sharedPreference = ContextCatcher.getContext().getSharedPreferences(
                        "USER_DATA",
                        Context.MODE_PRIVATE
                    )
                    val editor = sharedPreference.edit()
                    editor.putString("USER_ID", userId.value.toString())
                    editor.putString("USER_TOKEN", userToken.value)
                    editor.commit()
                    globalUserId = userId.value

                    toastChannel.tryEmit("Signed in successfully")

                    navChannel.tryEmit("all_leagues")

                } else {
                    _toastChannel.emit("Sign in failed")
                }
            }
        }
    }

    private fun validateData(): Boolean {
        if(username.value.isEmpty() || password.value.isEmpty()) {
            _toastChannel.tryEmit("Please fill all fields")
            return false
        }
        return true
    }

    fun signUp(){
        if(validateData()){
            viewModelScope.launch(Dispatchers.IO) {
                val response = repository.signUp(username.value, password.value)
                if (response.isSuccessful) {
                    _toastChannel.emit("Signed up successfully")
                } else {
                    _toastChannel.emit("Sign up failed,\n Username already in use!")
                }
                clearData()
            }
        }
    }

    fun onUsernameChange(newUsername: String) {
        username.value = newUsername
        Log.d("username", newUsername)
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun clearData() {
        username.value = ""
        password.value = ""
    }
}


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
    val newDate: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    val newTime: MutableStateFlow<LocalTime> = MutableStateFlow(LocalTime.now())

    val toastChannel = MutableSharedFlow<String>(extraBufferCapacity = 1)



    //TODO: handle Datetime
    fun updateMatch(matchDetails: MatchDisplay){
        viewModelScope.launch() {
            val data = UpdateMatchDTO(
                newHostScore.value.toInt(),
                newGuestScore.value.toInt(),
            )
            val response = repository.updateMatch(matchDetails.matchId, data)
            clearForm()
            if (response == "200") toastChannel.emit("Zaktualizowano mecz!")
            else toastChannel.emit("Wystąpił błąd! Spróbuj ponownie!")
        }
    }

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

    fun onDateChange(newDate: LocalDate) {
        this.newDate.value = newDate
    }

    fun onTimeChange(newTime: LocalTime) {
        this.newTime.value = newTime
        Log.d("datetime", newDate.toString() + "T" + DateTimeFormatter.ofPattern("hh:mm").format(this.newTime.value))
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
        if (newHost.value.teamId == -1
            || newGuest.value.teamId == -1
            || newAddress.value == ""
        ) return false
        val tmpDate = (newDate.value.toString()
                + "T" + newTime.value.format(DateTimeFormatter.ofPattern("hh:mm")))
        viewModelScope.launch() {
            val leagueId = leagueState.first().leagueId
            val response = repository.addMatch(

                AddMatchDTO(
                    league = leagueId,
                    homeTeamId = newHost.value.teamId,
                    homeTeamScore = newHostScore.value.toIntOrNull() ?: 0,
                    awayTeamId = newGuest.value.teamId,
                    awayTeamScore = newGuestScore.value.toIntOrNull() ?: 0,
                    date = tmpDate, //TODO: FIX ME!
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