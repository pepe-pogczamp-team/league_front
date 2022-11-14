package com.fgieracki.leagueplanner.ui.components

import android.annotation.SuppressLint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fgieracki.leagueplanner.R
import com.fgieracki.leagueplanner.ui.leaguelist.ScreenAllLeagues
import com.fgieracki.leagueplanner.ui.leaguelist.ScreenLeagueMatches
import com.fgieracki.leagueplanner.ui.leaguelist.ScreenLeagueTeams
import com.fgieracki.leagueplanner.ui.leaguelist.ScreenMyLeagues
import com.fgieracki.leagueplanner.ui.theme.LeagueBlue

@Composable
fun Navigation() {
    val navController = rememberNavController()

    fun backToAllLeagues() { //TODO: repair me
        navController.popBackStack("all_leagues", inclusive = false)
    }

    fun navigateToMyLeagues() {
        navController.navigate("my_leagues") {
            launchSingleTop = true
            popUpTo("all_leagues") {
                inclusive = false
            }
        }
    }

    fun navigateToAllLeagues() {
        navController.navigate("all_leagues") {
            launchSingleTop = true
            popUpTo("my_leagues") {
                inclusive = true
            }
        }
    }

    fun navigateToLeagueTeams(leagueId: Int) {
        navController.navigate("league_teams/$leagueId") {
            launchSingleTop = true
            popUpTo("all_leagues") {
                inclusive = false
            }
        }
    }

    fun navigateToLeagueMatches(leagueId: Int) {
        navController.navigate("league_matches/$leagueId") {
            launchSingleTop = true
            popUpTo("all_leagues") {
                inclusive = false
            }
        }
    }

    NavHost(navController = navController, startDestination = "all_leagues") {
        composable("all_leagues") {
            ScreenAllLeagues(
                onNavigateToMyLeagues = { navigateToMyLeagues() },
                onNavigateToLeagueTeams = { leagueId -> navigateToLeagueTeams(leagueId) })
        }

        composable("my_leagues") {
            ScreenMyLeagues(
                onNavigateToAllLeagues = { navigateToAllLeagues() },
                onNavigateToLeagueTeams = { leagueId -> navigateToLeagueTeams(leagueId) })
        }

        composable("league_teams/{leagueId}") {
            val leagueId = it.arguments?.getString("leagueId")?.toInt() ?: 0
            ScreenLeagueTeams(leagueId,
                onNavigateToLeagueTeams = { navigateToLeagueTeams(leagueId) },
                onNavigateToLeagueMatches = { navigateToLeagueMatches(leagueId) },
                onBackClick = { backToAllLeagues() })
        }

        composable("league_matches/{leagueId}") {
            val leagueId = it.arguments?.getString("leagueId")?.toInt() ?: 0
            ScreenLeagueMatches(leagueId,
                onNavigateToLeagueTeams = { navigateToLeagueTeams(leagueId) },
                onNavigateToLeagueMatches = { navigateToLeagueMatches(leagueId) },
                onBackClick = { backToAllLeagues() })
        }

    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueListNavBar(
    screen: Int,
    onNavigateToMyLeagues: () -> Unit,
    onNavigateToAllLeagues: () -> Unit
) {
    var selectedItem by remember { mutableStateOf(screen) }
    val items = listOf("Moje", "Wszystkie")
    val icons = listOf(
        painterResource(id = R.drawable.ic_baseline_star_24),
        painterResource(id = R.drawable.ic_baseline_public_24)
    )
    NavigationBar {
        val iconColor = LeagueBlue
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item, tint = iconColor) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index

                    if (index == 0) onNavigateToMyLeagues()
                    else if (index == 1) onNavigateToAllLeagues()
                }
            )
        }
    } // NavigationBar
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueNavBar(
    screen: Int, leagueId: Int,
    onNavigateToLeagueMatches: () -> Unit,
    onNavigateToLeagueTeams: () -> Unit
) {
    var selectedItem by remember { mutableStateOf(screen) }
    val items = listOf("Drużyny", "Mecze")
    val icons = listOf(
        painterResource(id = R.drawable.ic_baseline_groups_24),
        painterResource(id = R.drawable.ic_baseline_leaderboard_24)
    )
    NavigationBar {
        val iconColor = LeagueBlue
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item, tint = iconColor) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index

                    if (index == 0)
                        onNavigateToLeagueTeams()
                    else if (index == 1)
                        onNavigateToLeagueMatches()

//                        if(navController.currentBackStackEntry?.destination?.route?.contains("league_teams") == false){
//                            navController.popBackStack()
//                        }
//                        navController.navigate("league_teams/${leagueId}"){
//                            launchSingleTop = true
//                            popUpTo("all_leagues"){
////                                Log.d("NAV_DEBUG", "league_teams/$leagueId")
//                                inclusive = false
//                            }
//                        }
//                    } else
                    //                    {
////                        navController.popBackStack()
//                        navController.navigate("league_matches/${leagueId}"){
////                            Log.d("NAV_DEBUG2", "league_matches/${leagueId}")
//                            launchSingleTop = true
//                            popUpTo("all_leagues"){
//                                inclusive = false
//                            }
//                        }
//                    }
                }
            )
        }
    } // NavigationBar
}


