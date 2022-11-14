package com.fgieracki.leagueplanner.ui.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fgieracki.leagueplanner.R
import com.fgieracki.leagueplanner.ui.leaguelist.ScreenAllLeagues
import com.fgieracki.leagueplanner.ui.leaguelist.ScreenLeagueMatches
import com.fgieracki.leagueplanner.ui.leaguelist.ScreenLeagueTeams
import com.fgieracki.leagueplanner.ui.leaguelist.ScreenMyLeagues
import com.fgieracki.leagueplanner.ui.theme.LeagueBlue

//@Composable
//fun Navigation(){
//    val navController = rememberNavController()
//    NavHost(navController, startDestination = "all"){
//        composable("all"){
//            ScreenAllLeagues(onNavigateToYours = { navController.navigate("your"){
//                launchSingleTop = true
//                popUpTo("all"){
//                    inclusive = false
//                }
//            } })
//        }
//        composable("your"){
//            ScreenYourLeagues(
//                onNavigateToAll = { navController.navigate("all"){
//                    launchSingleTop = true
//                    popUpTo("your"){
//                        inclusive = true
//                    }
//                } })
//        }
//    }
//}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "all_leagues") {
        composable("all_leagues"){
            ScreenAllLeagues(navController = navController)
        }

        composable("my_leagues"){
            ScreenMyLeagues(navController = navController)
        }

        composable("league_teams/{leagueId}"){
            val leagueId = it.arguments?.getString("leagueId")?.toInt() ?: 0
            ScreenLeagueTeams(leagueId, navController = navController)
        }

        composable("league_matches/{leagueId}"){
            val leagueId = it.arguments?.getString("leagueId")?.toInt() ?: 0
            ScreenLeagueMatches(leagueId, navController = navController)
        }

    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueListNavBar(screen: Int, navController: NavController){
    var selectedItem by remember { mutableStateOf(screen) }
    val items = listOf("Moje", "Wszystkie")
    val icons = listOf(painterResource(id = R.drawable.ic_baseline_star_24),
        painterResource(id = R.drawable.ic_baseline_public_24))
    NavigationBar {
        val iconColor = LeagueBlue
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item, tint = iconColor) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index

                    if(index == 0){ //TODO; repair this
                        navController.navigate("my_leagues"){
                            launchSingleTop = true
                            popUpTo("all_leagues"){
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate("all_leagues"){
                            launchSingleTop = true
                            popUpTo("my_leagues"){
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }
    } // NavigationBar
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueNavBar(screen: Int, leagueId: Int, navController: NavController){
    var selectedItem by remember { mutableStateOf(screen) }
    val items = listOf("Drużyny", "Mecze")
    val icons = listOf(painterResource(id = R.drawable.ic_baseline_groups_24),
        painterResource(id = R.drawable.ic_baseline_leaderboard_24))
    NavigationBar {
        val iconColor = LeagueBlue
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item, tint = iconColor) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index

                    if(index == 0){
                        navController.navigate("league_teams/${leagueId}"){
                            launchSingleTop = true
                            popUpTo("all_leagues"){
                                inclusive = false
                            }
                        }
                    } else {
                        navController.navigate("league_matches/${leagueId}"){
                            launchSingleTop = true
                            popUpTo("all_leagues"){
                                inclusive = false
                            }
                        }
                    }
                }
            )
        }
    } // NavigationBar
}


