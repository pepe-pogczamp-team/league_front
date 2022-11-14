package com.fgieracki.leagueplanner.ui.leaguelist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fgieracki.leagueplanner.data.mappers.mapMatchAndTeamToMatchDisplay
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.MatchDisplay
import com.fgieracki.leagueplanner.data.model.Team
import com.fgieracki.leagueplanner.ui.components.*
import com.fgieracki.leagueplanner.ui.theme.*

val userId: Int = 1

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
) {
    val viewModel: LeagueListViewModel = viewModel()
    val leagues = viewModel.leaguesState.collectAsState().value
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
) {
    val viewModel: LeagueListViewModel = viewModel()
    val leagues = viewModel.leaguesState.collectAsState().value
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
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButtonAdd(
                contentDesc = "Dodaj Ligę",
                onClick = {})
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLeagueTeams(
    leagueId: Int, //TODO: change me
    onNavigateToLeagueMatches: () -> Unit,
    onNavigateToLeagueTeams: () -> Unit = {},
    onBackClick: () -> Unit
) {
    val viewModel: TeamsAndMatchesViewModel = viewModel()
    LaunchedEffect(leagueId) { viewModel.refreshTeams(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshLeague(leagueId) }

    val league = viewModel.leagueState.collectAsState().value
    val teams = viewModel.teamsState.collectAsState().value

    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = league.name, showBackButton = true, clickAction = onBackClick) },
        bottomBar = {
            LeagueNavBar(
                screen = 0, leagueId,
                onNavigateToLeagueTeams = onNavigateToLeagueTeams,
                onNavigateToLeagueMatches = onNavigateToLeagueMatches
            )
        },
        content = { TeamList(teams, modifier = Modifier.padding(it)) },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (userId == league.ownerId) FloatingActionButtonAdd(
                contentDesc = "Dodaj Drużynę",
                onClick = {})
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLeagueMatches(
    leagueId: Int,
    onNavigateToLeagueMatches: () -> Unit,
    onNavigateToLeagueTeams: () -> Unit = {},
    onBackClick: () -> Unit
) {
    val viewModel: TeamsAndMatchesViewModel = viewModel()
    LaunchedEffect(leagueId) { viewModel.refreshMatches(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshTeams(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshLeague(leagueId) }

    val league = viewModel.leagueState.collectAsState().value
    val teams = viewModel.teamsState.collectAsState().value
    val matches = viewModel.matchesState.collectAsState().value

    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = league.name, showBackButton = true, clickAction = onBackClick) },
        bottomBar = {
            LeagueNavBar(
                screen = 1, leagueId,
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


@Composable
fun MatchList(matches: List<Match>, teams: List<Team>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        matches.forEach { match ->
            MatchItem(mapMatchAndTeamToMatchDisplay(match, teams))
        }
    }
}

@Composable
fun MatchItem(match: MatchDisplay) {
    Log.d("MatchItem", match.toString())
    val team1 = match.homeTeamName
    val team2 = match.awayTeamName
    val scores = if(match.homeTeamScore == null && match.awayTeamScore == null) "? - ?" else "${match.homeTeamScore} - ${match.awayTeamScore}"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(color = Gray, shape = RoundedCornerShape(8.dp))
            .border(1.dp, color = LeagueBlue, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "$team1",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .align(Alignment.Start)
            )
            Text(
                text = "$scores",
                color = Color.LightGray,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "$team2",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .align(Alignment.End)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    MatchItem()
//    TeamItem(Team("test", 1, 1, 1))
}

