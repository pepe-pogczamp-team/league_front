package com.fgieracki.leagueplanner.ui.leaguelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import com.fgieracki.leagueplanner.data.mappers.MatchAndTeamtoMatchDisplay
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.MatchDisplay
import com.fgieracki.leagueplanner.data.model.Team
import com.fgieracki.leagueplanner.ui.components.LeagueList
import com.fgieracki.leagueplanner.ui.components.LeagueListNavBar
import com.fgieracki.leagueplanner.ui.components.Navigation
import com.fgieracki.leagueplanner.ui.theme.*
import com.fgieracki.leagueplanner.ui.components.*
import com.fgieracki.leagueplanner.ui.theme.LeaguePlannerTheme



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
fun ScreenAllLeagues(navController: NavController){
    val viewModel: LeagueListViewModel = viewModel()
    val leagues = viewModel.leaguesState.collectAsState().value
    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = "Wszystkie Ligi") },
        bottomBar = { LeagueListNavBar(screen = 1, navController = navController) },
        content = { LeagueList(leagues, -1, modifier = Modifier.padding(it), navController = navController) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMyLeagues(ownerId: Int = 1,
                    navController: NavController){
    val viewModel: LeagueListViewModel = viewModel()
    val leagues = viewModel.leaguesState.collectAsState().value
    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = "Moje Ligi") },
        bottomBar = { LeagueListNavBar(screen = 0, navController = navController) },
        content = { LeagueList(leagues, ownerId, modifier = Modifier.padding(it), navController = navController)},
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FABAddLeague() },
    )
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLeagueTeams(leagueId: Int, //TODO: change me
                      navController: NavController){
    val viewModel: TeamsAndMatchesViewModel = viewModel()
    LaunchedEffect(leagueId) { viewModel.refreshTeams(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshLeague(leagueId) }

    val league = viewModel.leagueState.collectAsState().value
    val teams = viewModel.teamsState.collectAsState().value

    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = league.name) },
        bottomBar = { LeagueNavBar(screen = 0, leagueId, navController = navController) },
        content = { TeamList(teams, modifier = Modifier.padding(it)) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLeagueMatches(leagueId: Int, navController: NavController){
    val viewModel: TeamsAndMatchesViewModel = viewModel()
    LaunchedEffect(leagueId) { viewModel.refreshMatches(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshTeams(leagueId) }
    LaunchedEffect(leagueId) { viewModel.refreshLeague(leagueId) }

    val league = viewModel.leagueState.collectAsState().value
    val teams = viewModel.teamsState.collectAsState().value
    val matches = viewModel.matchesState.collectAsState().value

    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = league.name) },
        bottomBar = { LeagueNavBar(screen = 1, leagueId, navController = navController) },
        content = { MatchList(matches, teams, modifier = Modifier.padding(it)) },
    )
}

@Composable
fun LeagueItem(league: League, clickAction: () -> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .background(color = Gray, shape = RoundedCornerShape(8.dp))
        .border(1.dp, color = LeagueBlue, shape = RoundedCornerShape(8.dp))
        .clickable {
            clickAction()
        }


    ) {
        Text(text = league.name,
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart)

        )
    }

}

@Composable
fun MatchList(matches: List<Match>, teams: List<Team>, modifier: Modifier = Modifier){
    Column(modifier = modifier) {
        matches.forEach { match ->
            MatchItem(MatchAndTeamtoMatchDisplay(match, teams))
        }
    }
}

@Composable
fun MatchItem(match: MatchDisplay){
    val team1 = "test1"
    val team2 = "test2"
    val scores = "0 - 0"
//    val scores = if(score1 == null && score2 == null) "? - ?" else "$score1 - $score2"

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .background(color = Gray, shape = RoundedCornerShape(8.dp))
        .border(1.dp, color = LeagueBlue, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "$team1",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .align(Alignment.Start)
                )
            Text(text = "$scores",
                color = Color.LightGray,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(text = "$team2",
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

