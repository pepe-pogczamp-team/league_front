package com.fgieracki.leagueplanner.ui.Application.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Team
import com.fgieracki.leagueplanner.ui.Application.TeamsViewModel
import com.fgieracki.leagueplanner.ui.components.*
import com.fgieracki.leagueplanner.ui.theme.LightGray
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLeagueTeams(
    leagueId: Int, //TODO: change me
    onNavigateToLeagueMatches: () -> Unit,
    onNavigateToLeagueTeams: () -> Unit = {},
    onBackClick: () -> Unit,
    viewModel: TeamsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val showAddTeamDialog = remember { mutableStateOf(false) }
    val showTeamDetailsDialog = remember { mutableStateOf(false) }
    val teamDetails = remember {
        mutableStateOf(Team(name = "", teamId = -1, leagueId = -1, points = -1, city = ""))
    }

    LaunchedEffect(leagueId) { viewModel.refreshTeams(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshLeague(leagueId) }
    val league = viewModel.leagueState.collectAsState( initial = League())
    val teams = viewModel.teamsState.collectAsState( initial = emptyList())
    val userId = viewModel.userId

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.toastChannel.collectLatest {
            Toast.makeText(context, it,
                Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = league.value.name, showBackButton = true, clickAction = onBackClick) },
        bottomBar = {
            LeagueNavBar(
                screen = 0,
                leagueId = leagueId,
                onNavigateToLeagueTeams = onNavigateToLeagueTeams,
                onNavigateToLeagueMatches = onNavigateToLeagueMatches
            )
        },
        content = {
            TeamList(teams.value, modifier = Modifier.padding(it), onItemClick = {
                teamDetails.value = it
                showTeamDetailsDialog.value = true }
            )
            if(showAddTeamDialog.value)
                AddTeamDialog(onDismiss = { showAddTeamDialog.value = false },
                    teamName = viewModel.newTeamName.collectAsState().value,
                    onTeamNameChange = { viewModel.onTeamNameChange(it) },
                    teamCity = viewModel.newTeamCity.collectAsState().value,
                    onTeamCityChange = { viewModel.onTeamCityChange(it)},
                    onSubmit = { viewModel.addTeam() })
            else if(showTeamDetailsDialog.value){
                TeamDetailsDialog(onDismiss = { showTeamDetailsDialog.value = false }, team = teamDetails.value)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (userId == league.value.ownerId) FloatingActionButtonAdd(
                contentDesc = "Dodaj Drużynę",
                onClick = {
                    showAddTeamDialog.value = true
                })
        },
    )
}