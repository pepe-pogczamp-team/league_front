package com.fgieracki.leagueplanner.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fgieracki.leagueplanner.ScreenAllLeagues
import com.fgieracki.leagueplanner.ScreenYourLeagues
import com.fgieracki.leagueplanner.data.League
import com.fgieracki.leagueplanner.ui.theme.DarkGray
import com.fgieracki.leagueplanner.ui.theme.Gray
import com.fgieracki.leagueplanner.ui.theme.LeagueBlue

@Composable
fun TopBar(name: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = DarkGray)

        ,
    ) {
        Text(text = name.uppercase(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)

        )
    }
}

@Composable
fun LeagueList (leagues: List<League>, ownerId: Int){
    LazyColumn{
        if(ownerId == -1){
            items(leagues.size){
                LeagueItem(leagues[it])
            }
        } else {
            items(leagues.size){
                if(leagues[it].ownerId == ownerId){
                    LeagueItem(leagues[it])
                }
            }
        }
    }
}


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
fun NavBar(screen: Int, containerColor: Color = DarkGray, iconColor: Color = Color.White,
           contentColor: Color = Color.White,
           onNavigateToAll: () -> Unit = {},
           onNavigateToYours: () -> Unit = {}){
    var selectedItem by remember { mutableStateOf(screen) }
    val items = listOf("Twoje", "Wszystkie")

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    if(index == 0){
                        Icon(Icons.Filled.Star, contentDescription = "Twoje", tint = iconColor)
                    } else {
                        Icon(painter = painterResource(
                            id = com.fgieracki.leagueplanner.R.drawable.ic_baseline_public_24),
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
fun LeagueItem(league: League){
    Box(modifier = Modifier
        .fillMaxWidth()

        .padding(horizontal = 16.dp, vertical = 8.dp)
        .background(color = Gray, shape = RoundedCornerShape(8.dp))
        .border(1.dp, color = LeagueBlue, shape = RoundedCornerShape(8.dp))


    ) {
        Text(text = league.name,
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart)

        )
    }

}