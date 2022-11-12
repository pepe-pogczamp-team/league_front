package com.fgieracki.leagueplanner.ui.theme.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fgieracki.leagueplanner.R
import com.fgieracki.leagueplanner.ScreenAllLeagues
import com.fgieracki.leagueplanner.ScreenYourLeagues
import com.fgieracki.leagueplanner.ui.theme.DarkGray

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController, startDestination = "all"){
        composable("all"){
            ScreenAllLeagues(onNavigateToYours = { navController.navigate("your"){
                launchSingleTop = true
                popUpTo("all"){
                    inclusive = false
                }
            } })
        }
        composable("your"){
            ScreenYourLeagues(
                onNavigateToAll = { navController.navigate("all"){
                    launchSingleTop = true
                    popUpTo("your"){
                        inclusive = true
                    }
                } })
        }
    }
}



@Composable
fun LeagueNavBar(screen: Int, containerColor: Color = DarkGray, iconColor: Color = MaterialTheme.colorScheme.primary,
                 contentColor: Color = MaterialTheme.colorScheme.primary,
                 onNavigateToAll: () -> Unit = {},
                 onNavigateToYours: () -> Unit = {}){
    var selectedItem by remember { mutableStateOf(screen) }
    val items = listOf("Twoje", "Wszystkie")

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    if(index == 0){
                        println("icc= $iconColor")
                        Icon(Icons.Filled.Star, contentDescription = "Twoje", tint = iconColor)
                    } else {
                        Icon(painter = painterResource(
                            id = R.drawable.ic_baseline_public_24),
                            contentDescription = "Wszystkie", tint = iconColor)
                    }
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index
                    if(index == 0){
                        onNavigateToYours()
                    } else {
                        onNavigateToAll()
                    }

                }
            )
        }
    }
}


@Composable
fun FABAddLeague(modifier : Modifier = Modifier){
    FloatingActionButton(
        onClick = { /*TODO*/ },
        modifier = modifier
            .padding(16.dp)
        ,
//        backgroundColor = LeagueBlue,
        contentColor = MaterialTheme.colorScheme.primary,
        elevation = FloatingActionButtonDefaults.elevation(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "Add League"
        )
    }
}


