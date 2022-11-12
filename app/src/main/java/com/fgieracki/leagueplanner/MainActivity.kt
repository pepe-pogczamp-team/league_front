package com.fgieracki.leagueplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fgieracki.leagueplanner.data.League
import com.fgieracki.leagueplanner.data.Team
import com.fgieracki.leagueplanner.ui.theme.*
import com.fgieracki.leagueplanner.ui.theme.components.*

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
           Navigation()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenAllLeagues(onNavigateToYours: () -> Unit){
    LeaguePlannerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = LightGray
        ) {
            Column {
                TopBar(name = "Wszystkie Ligi")
                Box(modifier = Modifier.weight(1f)){
                    LeagueList(leagues, -1)
                }
                LeagueNavBar(screen = 1, onNavigateToYours = onNavigateToYours)
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ScreenAllLeagues(){
//    Scaffold(
//        topBar = { TopBar(name = "Wszystkie Ligi")}
////        bottomBar = { LeagueNavBar()}
//    ) {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = LightGray
//        ) {
//            LeagueList(leagues = leagues, ownerId = -1)
//        }
//    }
//}

@Composable
fun ScreenYourLeagues(ownerId: Int = 1, onNavigateToAll: () -> Unit){
    LeaguePlannerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = LightGray
        ){
            Column {
                TopBar(name = "Twoje Ligi")
                Box(modifier = Modifier.weight(1f)){
                    LeagueList(leagues, ownerId)
                    FABAddLeague(modifier = Modifier.align(Alignment.BottomEnd))
                }

                LeagueNavBar(screen = 0, onNavigateToAll = onNavigateToAll)
            }

        }
    }
}



@Composable
fun ScreenLeagueTeams(league: League, teams: List<Team>){
    LeaguePlannerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = LightGray
        ) {
            Column {
                TopBar(name = league.name)
                Box(modifier = Modifier.weight(1f)){
                    TeamList(teams = teams)
                }
                LeagueNavBar(screen = 1) //TODO: change ME!
            }

        }
    }
}






@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ScreenAllLeagues({})
}

//modifier: weight