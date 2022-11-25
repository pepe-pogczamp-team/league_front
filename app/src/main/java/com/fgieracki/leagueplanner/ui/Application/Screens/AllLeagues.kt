package com.fgieracki.leagueplanner.ui.Application.Screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.fgieracki.leagueplanner.ui.Application.LeagueListViewModel
import com.fgieracki.leagueplanner.ui.components.LeagueList
import com.fgieracki.leagueplanner.ui.components.LeagueListNavBar
import com.fgieracki.leagueplanner.ui.components.TopBar
import com.fgieracki.leagueplanner.ui.theme.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenAllLeagues(
    onNavigateToMyLeagues: () -> Unit,
    onNavigateToAllLeagues: () -> Unit = {},
    onNavigateToLeagueTeams: (Int) -> Unit,
    viewModel: LeagueListViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val leagues = viewModel.leaguesState.collectAsState(initial = emptyList()).value
    val userId = viewModel.userId
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