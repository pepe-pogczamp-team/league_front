package com.fgieracki.leagueplanner.ui.Application.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.fgieracki.leagueplanner.ui.Application.LeagueListViewModel
import com.fgieracki.leagueplanner.ui.components.*
import com.fgieracki.leagueplanner.ui.theme.LightGray
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMyLeagues(
    onNavigateToMyLeagues: () -> Unit = {},
    onNavigateToAllLeagues: () -> Unit,
    onNavigateToLeagueTeams: (Int) -> Unit,
    viewModel: LeagueListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val showDialog = remember { mutableStateOf(false) }
    val leagues = viewModel.leaguesState.collectAsState(initial = emptyList()).value
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