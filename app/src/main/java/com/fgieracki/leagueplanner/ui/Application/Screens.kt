package com.fgieracki.leagueplanner.ui.Application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.ui.components.*
import com.fgieracki.leagueplanner.ui.theme.*

const val userId: Int = 1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeaguePlannerTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LightGray,
                ) {
                    Navigation()
                } // Surface
            } // LeaguePlannerTheme
        } //setContent
    } // onCreate
} // MainActivity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenAllLeagues(
    onNavigateToMyLeagues: () -> Unit,
    onNavigateToAllLeagues: () -> Unit = {},
    onNavigateToLeagueTeams: (Int) -> Unit,
    viewModel: LeagueListViewModel = viewModel(),
) {
    val leagues = viewModel.leaguesState.collectAsState(initial = emptyList()).value
    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = "Wszystkie Ligi", clickAction = {}) },
        bottomBar = {
            LeagueListNavBar(
                screen = 1,
                onNavigateToMyLeagues = onNavigateToMyLeagues,
                onNavigateToAllLeagues = onNavigateToAllLeagues
            )
        },
        content = {
            LeagueList(
                leagues,
                -1,
                modifier = Modifier.padding(it),
                onNavigateToLeague = { id -> onNavigateToLeagueTeams.invoke(id) })
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMyLeagues(
    ownerId: Int = userId,
    onNavigateToMyLeagues: () -> Unit = {},
    onNavigateToAllLeagues: () -> Unit,
    onNavigateToLeagueTeams: (Int) -> Unit,
    viewModel: LeagueListViewModel = viewModel()
) {
    val showDialog = remember { mutableStateOf(false) }
    val leagues = viewModel.leaguesState.collectAsState(initial = emptyList()).value

    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = "Moje Ligi", clickAction = {}) },
        bottomBar = {
            LeagueListNavBar(
                screen = 0,
                onNavigateToMyLeagues = onNavigateToMyLeagues,
                onNavigateToAllLeagues = onNavigateToAllLeagues
            )
        },
        content = {
            LeagueList(leagues, userId, modifier = Modifier.padding(it),
                onNavigateToLeague = { id -> onNavigateToLeagueTeams.invoke(id) })
            if(showDialog.value) {
                AddLeagueDialog(onDismiss = { showDialog.value = false },
                    leagueName = viewModel.newLeagueName.collectAsState().value,
                    onLeagueNameChange = { viewModel.onLeagueNameChange(it) },
                    onSubmit = {viewModel.addLeague() })
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButtonAdd(
                contentDesc = "Dodaj Ligę",
                onClick = {
                    showDialog.value = true
                })
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLeagueTeams(
    leagueId: Int, //TODO: change me
    onNavigateToLeagueMatches: () -> Unit,
    onNavigateToLeagueTeams: () -> Unit = {},
    onBackClick: () -> Unit,
    viewModel: TeamsAndMatchesViewModel = viewModel()
) {
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(leagueId) { viewModel.refreshTeams(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshLeague(leagueId) }
    val league = viewModel.leagueState.collectAsState( initial = League())
    val teams = viewModel.teamsState.collectAsState( initial = emptyList())

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
        content = { TeamList(teams.value, modifier = Modifier.padding(it))
                  if(showDialog.value)
                      AddTeamDialog(onDismiss = { showDialog.value = false },
                          teamName = viewModel.newTeamName.collectAsState().value,
                          onTeamNameChange = { viewModel.onTeamNameChange(it) },
                          teamCity = viewModel.newTeamCity.collectAsState().value,
                          onTeamCityChange = { viewModel.onTeamCityChange(it)},
                          onSubmit = {viewModel.addTeam() })
                  },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (userId == league.value.ownerId) FloatingActionButtonAdd(
                contentDesc = "Dodaj Drużynę",
                onClick = {
                    showDialog.value = true
                })
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLeagueMatches(
    leagueId: Int,
    onNavigateToLeagueMatches: () -> Unit,
    onNavigateToLeagueTeams: () -> Unit = {},
    onBackClick: () -> Unit,
    viewModel: TeamsAndMatchesViewModel = viewModel()
) {
//    val viewModel: TeamsAndMatchesViewModel = viewModel()
    LaunchedEffect(leagueId) { viewModel.refreshMatches(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshTeams(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshLeague(leagueId) }

    val league = viewModel.leagueState.collectAsState(initial = League()).value
    val teams = viewModel.teamsState.collectAsState(initial = emptyList()).value
    val matches = viewModel.matchesState.collectAsState(initial = emptyList()).value

    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = league.name, showBackButton = true, clickAction = onBackClick) },
        bottomBar = {
            LeagueNavBar(
                screen = 1,
                leagueId = leagueId,
                onNavigateToLeagueTeams = onNavigateToLeagueTeams,
                onNavigateToLeagueMatches = onNavigateToLeagueMatches
            )
        },
        content = { MatchList(matches, teams, modifier = Modifier.padding(it)) },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (userId == league.ownerId) FloatingActionButtonAdd(
                contentDesc = "Dodaj Mecz",
                onClick = {})
        },
    )
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    MatchItem()
//    TeamItem(Team("test", 1, 1, 1))
}

