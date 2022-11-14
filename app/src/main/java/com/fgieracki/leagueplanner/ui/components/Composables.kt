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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fgieracki.leagueplanner.R
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Team
import com.fgieracki.leagueplanner.ui.theme.DarkGray
import com.fgieracki.leagueplanner.ui.theme.Gray
import com.fgieracki.leagueplanner.ui.theme.LeagueBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(name: String, showBackButton: Boolean = false, clickAction: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = { clickAction() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = "Cofnij",
                        tint = Color.White
                    )
                }
            }
        }
    )
}

@Composable
fun LeagueList(
    leagues: List<League>,
    ownerId: Int,
    modifier: Modifier = Modifier,
    onNavigateToLeague: (Int) -> Unit
) {
    LazyColumn(modifier = modifier) {
        if (ownerId == -1) {
            items(leagues.size) {
                LeagueItem(leagues[it]) {
                    onNavigateToLeague(leagues[it].leagueId)
                }
            }
        } else {
            items(leagues.size) {
                if (leagues[it].ownerId == ownerId) {
                    LeagueItem(leagues[it]) {
                        onNavigateToLeague(leagues[it].leagueId)
                    }
                }
            }
        }
    }
}


@Composable
fun TeamList(teams: List<Team>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(teams.size) {
            TeamItem(teams[it])
        }
    }
}

@Composable
fun FloatingActionButtonAdd(
    modifier: Modifier = Modifier,
    contentDesc: String,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = { onClick() },
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
            contentDescription = contentDesc
        )
    }
}


@Composable
fun TeamItem(team: Team) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(color = Gray, shape = RoundedCornerShape(8.dp))
            .border(1.dp, color = LeagueBlue, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = team.name,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
            Text(
                text = team.points.toString(),
                color = Color.White,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.End
            )
        }
    }

}


@Composable
fun LeagueItem(league: League, clickAction: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .background(color = Gray, shape = RoundedCornerShape(8.dp))
        .border(1.dp, color = LeagueBlue, shape = RoundedCornerShape(8.dp))
        .clickable {
            clickAction()
        }


    ) {
        Text(
            text = league.name,
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart)

        )
    }

}