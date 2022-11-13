package com.fgieracki.leagueplanner.ui.leaguelist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fgieracki.leagueplanner.data.Repository
import com.fgieracki.leagueplanner.data.model.League
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeagueListViewModel(private val repository: Repository = Repository()) : ViewModel() {
     init {
         viewModelScope.launch(Dispatchers.IO) {
             val leagues = getLeagues()
             leaguesState.value = leagues
         }
     }

    val leaguesState: MutableState<List<League>> = mutableStateOf(emptyList<League>())

    private suspend fun getLeagues(): List<League> {
        return repository.getLeagues()
    }

}