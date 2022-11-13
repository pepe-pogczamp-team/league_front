package com.fgieracki.leagueplanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fgieracki.leagueplanner.R
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Team
import com.fgieracki.leagueplanner.ui.leaguelist.LeagueItem
import com.fgieracki.leagueplanner.ui.theme.DarkGray
import com.fgieracki.leagueplanner.ui.theme.Gray
import com.fgieracki.leagueplanner.ui.theme.LeagueBlue


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

@Composable
fun LeagueList (leagues: List<League>, ownerId: Int, modifier: Modifier = Modifier, navController: NavController) {
    LazyColumn(modifier = modifier){
        if(ownerId == -1){
            items(leagues.size){
                LeagueItem(leagues[it]){
                    navController.navigate("league_teams/${leagues[it].leagueId}")
                }
            }
        } else {
            items(leagues.size){
                if(leagues[it].ownerId == ownerId){
                    LeagueItem(leagues[it]){
                        navController.navigate("league_teams/${leagues[it].leagueId}")
                    }
                }
            }
        }
    }
}



@Composable
fun TeamList(teams: List<Team>, modifier: Modifier = Modifier){
    LazyColumn(modifier = modifier
//        .padding(16.dp)
    ){
        items(teams.size){
            TeamItem(teams[it])
        }
    }
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

@Composable
fun TeamItem(team: Team){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .background(color = Gray, shape = RoundedCornerShape(8.dp))
        .border(1.dp, color = LeagueBlue, shape = RoundedCornerShape(8.dp))
    ){
        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = team.name,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
            Text(text = team.points.toString(),
                color = Color.White,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.End
            )
        }
    }

}