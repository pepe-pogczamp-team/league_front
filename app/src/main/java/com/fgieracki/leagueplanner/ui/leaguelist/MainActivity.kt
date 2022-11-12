package com.fgieracki.leagueplanner.ui.leaguelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fgieracki.leagueplanner.R
import com.fgieracki.leagueplanner.data.League
import com.fgieracki.leagueplanner.ui.components.LeagueList
import com.fgieracki.leagueplanner.ui.components.LeagueNavBar
import com.fgieracki.leagueplanner.ui.components.Navigation
import com.fgieracki.leagueplanner.ui.theme.*
import com.fgieracki.leagueplanner.ui.components.*

val leagues = listOf<League>(League("test 1",1, 1,),
    League("test 2", 2, 2),
    League("test 3", 3, 3),
    League("test 4", 4, 4),
    League("test 5", 5, 5),
    League("test 6", 6, 6),
    League("test 7", 7, 1),
    League("test 8", 8, 8),
    League("test 9", 9, 9),
    League("test 10", 10, 10),
    League("test 11", 11, 11),
    League("test 12", 12, 12),
    League("test 13", 13, 1),
    League("test 14", 14, 14),
    League("test 15", 15, 1),
    League("test 16", 16, 16),
    League("test 17", 17, 17),
    League("test 18", 18, 18),
    League("test 19", 19, 19),
    League("test 20", 20, 20))

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeaguePlannerTheme {
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
fun TopBar(name: String) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkGray,
        ),
        title = {
            Text(
                name, fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenAllLeagues(allLeagues: List<League> = leagues, navController: NavController){
    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = "Wszystkie Ligi") },
        bottomBar = { LeagueNavBar(screen = 1, navController = navController) },
        content = { LeagueList(allLeagues, -1, modifier = Modifier.padding(it)) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMyLeagues(allLeagues: List<League> = leagues, ownerId: Int = 1,
                    navController: NavController){
    Scaffold(
        containerColor = LightGray,
        topBar = { TopBar(name = "Moje Ligi") },
        bottomBar = { LeagueNavBar(screen = 0, navController = navController) },
        content = { LeagueList(allLeagues, ownerId, modifier = Modifier.padding(it)) },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FABAddLeague() },
    )
}



@Composable
fun FABAddLeague(modifier : Modifier = Modifier){
    FloatingActionButton(
        onClick = { /*TODO*/ },
        modifier = modifier
            .padding(16.dp)
            .background(Gray, shape = CircleShape)
            .border(1.dp, LeagueBlue, CircleShape),
        shape = CircleShape,
        containerColor = DarkGray,
        elevation = FloatingActionButtonDefaults.elevation(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            tint = LeagueBlue,
            contentDescription = "Add League"
        )
    }
}


//@Composable
//fun ScreenLeagueTeams(league: League, teams: List<Team>){
//    LeaguePlannerTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = LightGray
//        ) {
//            Column {
//                TopBar(name = league.name)
//                Box(modifier = Modifier.weight(1f)){
//                    TeamList(teams = teams)
//                }
//                LeagueNavBar(screen = 1) //TODO: change ME!
//            }
//
//        }
//    }
//}






@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    ScreenAllLeagues()
}