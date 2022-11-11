package com.fgieracki.leagueplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fgieracki.leagueplanner.data.League
import com.fgieracki.leagueplanner.ui.theme.*
import com.fgieracki.leagueplanner.ui.theme.components.LeagueList
import com.fgieracki.leagueplanner.ui.theme.components.NavBar
import com.fgieracki.leagueplanner.ui.theme.components.Navigation
import com.fgieracki.leagueplanner.ui.theme.components.TopBar

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
                NavBar(screen = 1, onNavigateToYours = onNavigateToYours)
            }

        }
    }
}

@Composable
fun ScreenYourLeagues(onNavigateToAll: () -> Unit){
    LeaguePlannerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = LightGray
        ) {
            Column {
                TopBar(name = "Twoje Ligi")
                Box(modifier = Modifier.weight(1f)){
                    LeagueList(leagues, 1)
                }
                NavBar(screen = 0, onNavigateToAll = onNavigateToAll)
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