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
import com.fgieracki.leagueplanner.data.model.MatchDisplay
import com.fgieracki.leagueplanner.ui.Application.MatchesViewModel
import com.fgieracki.leagueplanner.ui.components.*
import com.fgieracki.leagueplanner.ui.theme.LightGray
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLeagueMatches(
    leagueId: Int,
    onNavigateToLeagueMatches: () -> Unit,
    onNavigateToLeagueTeams: () -> Unit = {},
    onBackClick: () -> Unit,
    viewModel: MatchesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val showAddMatchDialog = remember { mutableStateOf(false) }
    val showMatchDetailsDialog = remember { mutableStateOf(false) }
    val matchDetails = remember {
        mutableStateOf(
            MatchDisplay(
                matchId = -1,
                leagueId = -1,
                homeTeamId = -1,
                awayTeamId = -1,
                homeTeamScore = 0,
                awayTeamScore = 0,
                matchDate = "",
                matchLocation = "",
                homeTeamName = "",
                awayTeamName = "",
                homeTeamCity = "",
                awayTeamCity = "",
            )
        )
    }

    LaunchedEffect(leagueId) { viewModel.refreshMatches(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshTeams(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshLeague(leagueId) }

    val league = viewModel.leagueState.collectAsState(initial = League()).value
    val teams = viewModel.teamsState.collectAsState(initial = emptyList()).value
    val matches = viewModel.matchesState.collectAsState(initial = emptyList()).value
    val userId = viewModel.userId
    val context = LocalContext.current
    val editable = league.ownerId == userId

    LaunchedEffect(Unit) {
        viewModel.toastChannel.collectLatest {
            Toast.makeText(
                context, it,
                Toast.LENGTH_LONG
            ).show()
        }
    }

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
        content = {
            MatchList(matches, teams, modifier = Modifier.padding(it), onItemClick = {
                matchDetails.value = it
                showMatchDetailsDialog.value = true
                viewModel.onHostScoreChange(matchDetails.value.homeTeamScore.toString())
                viewModel.onGuestScoreChange(matchDetails.value.awayTeamScore.toString())
            })

            if(showMatchDetailsDialog.value){
                MatchDetailsDialog(
                    matchDetails.value,
                    onDismiss = { showMatchDetailsDialog.value = false },
                    editable = editable,
                    onSave = { viewModel.updateMatch(matchDetails.value)
                        showMatchDetailsDialog.value = false},
                    hostScore = viewModel.newHostScore.collectAsState().value,
                    onHostScoreChange = {
                        if (!viewModel.onHostScoreChange(it))
                            Toast.makeText(
                                context, "Wynik musi być liczbą",
                                Toast.LENGTH_LONG
                            ).show()
                    },
                    guestScore = viewModel.newGuestScore.collectAsState().value,
                    onGuestScoreChange = {
                        if (!viewModel.onGuestScoreChange(it))
                            Toast.makeText(
                                context, "Wynik musi być liczbą",
                                Toast.LENGTH_LONG
                            ).show()
                    },
                )
            }

            if (showAddMatchDialog.value) {
                AddMatchDialog(
                    //TODO: EDIT ME
                    teams = teams,
                    onDismiss = {
                        showAddMatchDialog.value = false
                        viewModel.clearForm()
                    },
                    onSubmit = {
                        if (!viewModel.addMatch())
                            Toast.makeText(
                                context, "Wypełnij wszystkie pola!",
                                Toast.LENGTH_LONG
                            ).show()
                        else showAddMatchDialog.value = false
                    },
                    host = viewModel.newHost.collectAsState().value,
                    onHostChange = {
                        if (!viewModel.onHostChange(it))
                            Toast.makeText(
                                context,
                                "Drużyna Gospodarzy musi być inna niż drużyna gości!",
                                Toast.LENGTH_LONG
                            ).show()
                    },
                    guest = viewModel.newGuest.collectAsState().value,
                    onGuestChange = {
                        if (!viewModel.onGuestChange(it))
                            Toast.makeText(
                                context,
                                "Drużyna Gospodarzy musi być inna niż drużyna gości!",
                                Toast.LENGTH_LONG
                            ).show()
                    },
                    hostScore = viewModel.newHostScore.collectAsState().value,
                    onHostScoreChange = {
                        if (!viewModel.onHostScoreChange(it))
                            Toast.makeText(
                                context, "Wynik musi być liczbą",
                                Toast.LENGTH_LONG
                            ).show()
                    },
                    guestScore = viewModel.newGuestScore.collectAsState().value,
                    onGuestScoreChange = {
                        if (!viewModel.onGuestScoreChange(it))
                            Toast.makeText(
                                context, "Wynik musi być liczbą",
                                Toast.LENGTH_LONG
                            ).show()
                    },
                    location = viewModel.newAddress.collectAsState().value,
                    onLocationChange = { viewModel.onLocationChange(it) },
                    date = viewModel.newDate.collectAsState().value,
                    onDateChange = { viewModel.onDateChange(it) },
                    time = viewModel.newTime.collectAsState().value,
                    onTimeChange = { viewModel.onTimeChange(it) },
                )
            }

        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (userId == league.ownerId) FloatingActionButtonAdd(
                contentDesc = "Dodaj Mecz",
                onClick = {
                    showAddMatchDialog.value = true
                })
        },
    )
}