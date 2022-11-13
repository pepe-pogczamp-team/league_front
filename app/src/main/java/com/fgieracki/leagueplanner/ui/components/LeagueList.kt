package com.fgieracki.leagueplanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Team
import com.fgieracki.leagueplanner.ui.theme.Gray
import com.fgieracki.leagueplanner.ui.theme.LeagueBlue


//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(color = DarkGray)
//
//        ,
//    ) {
//        Text(text = name.uppercase(),
//            color = Color.White,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .align(Alignment.Center)
//                .padding(16.dp)
//
//        )
//    }
//}





@Composable
fun LeagueList (leagues: List<League>, ownerId: Int, modifier: Modifier = Modifier){
    LazyColumn(modifier = modifier){
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

@Composable
fun TeamList(teams: List<Team>){
    LazyColumn(modifier = Modifier
        .padding(16.dp)){
        items(teams.size){
            TeamItem(teams[it])
        }
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
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
        }
    }

}