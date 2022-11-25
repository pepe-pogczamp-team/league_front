package com.fgieracki.leagueplanner.ui.Application.Screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.ui.Application.TeamsAndMatchesViewModel
import com.fgieracki.leagueplanner.ui.components.FloatingActionButtonAdd
import com.fgieracki.leagueplanner.ui.components.LeagueNavBar
import com.fgieracki.leagueplanner.ui.components.MatchList
import com.fgieracki.leagueplanner.ui.components.TopBar
import com.fgieracki.leagueplanner.ui.theme.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLeagueMatches(
    leagueId: Int,
    onNavigateToLeagueMatches: () -> Unit,
    onNavigateToLeagueTeams: () -> Unit = {},
    onBackClick: () -> Unit,
    viewModel: TeamsAndMatchesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
//    val viewModel: TeamsAndMatchesViewModel = viewModel()
    LaunchedEffect(leagueId) { viewModel.refreshMatches(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshTeams(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshLeague(leagueId) }

    val league = viewModel.leagueState.collectAsState(initial = League()).value
    val teams = viewModel.teamsState.collectAsState(initial = emptyList()).value
    val matches = viewModel.matchesState.collectAsState(initial = emptyList()).value
    val userId = viewModel.userId

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