package com.fgieracki.leagueplanner.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fgieracki.leagueplanner.R
import com.fgieracki.leagueplanner.data.mappers.mapMatchAndTeamToMatchDisplay
import com.fgieracki.leagueplanner.data.model.League
import com.fgieracki.leagueplanner.data.model.Match
import com.fgieracki.leagueplanner.data.model.MatchDisplay
import com.fgieracki.leagueplanner.data.model.Team
import com.fgieracki.leagueplanner.ui.theme.DarkGray
import com.fgieracki.leagueplanner.ui.theme.Gray
import com.fgieracki.leagueplanner.ui.theme.LeagueBlue
import com.fgieracki.leagueplanner.ui.theme.LightGray
import kotlin.coroutines.coroutineContext


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
fun TeamList(teams: List<Team>, modifier: Modifier = Modifier, onItemClick: (Team) -> Unit) {
    LazyColumn(
        modifier = modifier
    ) {
        items(teams.size) { lazyScope ->
            TeamItem(teams[lazyScope]) {
                onItemClick(it)
            }
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
fun TeamItem(team: Team, onItemClick: (Team) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(color = Gray, shape = RoundedCornerShape(8.dp))
            .border(1.dp, color = LeagueBlue, shape = RoundedCornerShape(8.dp))
            .clickable { onItemClick(team) }
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


@Composable
fun MatchList(matches: List<Match>, teams: List<Team>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        matches.forEach { match ->
            MatchItem(mapMatchAndTeamToMatchDisplay(match, teams))
        }
    }
}

@Composable
fun MatchItem(match: MatchDisplay) {
    Log.d("MatchItem", match.toString())
    val team1 = match.homeTeamName
    val team2 = match.awayTeamName
    val scores =
        if (match.homeTeamScore == null && match.awayTeamScore == null) "? - ?" else "${match.homeTeamScore} - ${match.awayTeamScore}"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(color = Gray, shape = RoundedCornerShape(8.dp))
            .border(1.dp, color = LeagueBlue, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "$team1",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .align(Alignment.Start)
            )
            Text(
                text = "$scores",
                color = Color.LightGray,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "$team2",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .align(Alignment.End)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddLeagueDialog(
    onDismiss: () -> Unit = {}, onSubmit: () -> Boolean,
    leagueName: String, onLeagueNameChange: (String) -> Unit = {}
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    val btnColor = if (isPressed) Color.Green else LeagueBlue
    val context = LocalContext.current

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true,
//            decorFitsSystemWindows = true,
        ),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
            ) {
                Text(
                    text = "Dodaj ligę",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = leagueName,
                    onValueChange = onLeagueNameChange,
                    label = { Text(text = "Nazwa ligi", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = btnColor,
                        unfocusedBorderColor = Gray,
                    ),
                )
                Button(
                    onClick = {
                        if (onSubmit())
                            onDismiss()
                        else {
                            Toast.makeText(
                                context, "Nazwa ligi nie może być pusta",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = btnColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Dodaj")
                }
            }
        }
    )
}

//@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TeamDetailsDialog(
    onDismiss: () -> Unit = {},
    team: Team
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
            ) {
                Text(
                    text = "Szczegóły drużyny",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = team.name,
                    enabled = false,
                    onValueChange = {},
                    label = { Text(text = "Nazwa drużyny", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Gray,
                        disabledTextColor = Color.LightGray,
                        //TODO: change color

                    )
                )
                OutlinedTextField(
                    value = team.city,
                    enabled = false,
                    onValueChange = {},
                    label = { Text(text = "Miasto", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Gray,
                        disabledTextColor = Color.LightGray,
                        //TODO: change color

                    )
                )
                Button(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LeagueBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Zamknij")
                }
            }
        }
    )
}


//@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddTeamDialog(
    onDismiss: () -> Unit = {}, onSubmit: () -> Boolean,
    teamName: String, onTeamNameChange: (String) -> Unit = {},
    teamCity: String, onTeamCityChange: (String) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    val btnColor = if (isPressed) Color.Green else LeagueBlue
    val context = LocalContext.current
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true,
//            decorFitsSystemWindows = true,
        ),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
            ) {
                Text(
                    text = "Dodaj drużynę",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = teamName,
                    onValueChange = onTeamNameChange,
                    label = { Text(text = "Nazwa drużyny", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = btnColor,
                        unfocusedBorderColor = Gray,
                    )
                )

                OutlinedTextField(
                    value = teamCity,
                    onValueChange = onTeamCityChange,
                    label = { Text(text = "Miasto", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = btnColor,
                        unfocusedBorderColor = Gray,
                    )

                )

                Button(
                    onClick = {
                        if (onSubmit())
                            onDismiss()
                        else {
                            Toast.makeText(
                                context, "Wypełnij wszystkie pola!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = btnColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Dodaj")
                }
            }
        }
    )
}


//@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddMatchDialog(
    teams: List<Team>,
    onDismiss: () -> Unit = {}, onSubmit: () -> Unit = {},
    host: Team, onHostChange: (Team) -> Unit = {},
    guest: Team, onGuestChange: (Team) -> Unit = {},
    hostScore: String, onHostScoreChange: (String) -> Unit = {},
    guestScore: String, onGuestScoreChange: (String) -> Unit = {},
//    date: String, onDateChange: (String) -> Unit = {},
    location: String, onLocationChange: (String) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    val btnColor = if (isPressed) Color.Green else LeagueBlue
    var hostExpanded by remember { mutableStateOf(false) }
    var guestExpanded by remember { mutableStateOf(false) }


    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true,
            decorFitsSystemWindows = true,
        ),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
            ) {
                Text(
                    text = "Dodaj Mecz",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )

                OutlinedTextField(
                    value = host.name,
                    onValueChange = {},
                    label = { Text(text = "Gospodarz*", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { hostExpanded = !hostExpanded },
                    enabled = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = btnColor,
                        unfocusedBorderColor = Gray,
                        disabledBorderColor = btnColor,
                        disabledTextColor = Color.White
                    )
                )
                DropdownMenu(
                    expanded = hostExpanded,
                    onDismissRequest = { hostExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    teams.forEach { team ->
                        DropdownMenuItem(onClick = {
                            onHostChange(team)
                            hostExpanded = false
                        },
                            text = { Text(text = team.name) })
                    }
                }

                Row {
                    OutlinedTextField(
                        value = hostScore,
                        onValueChange = onHostScoreChange,
                        label = { Text(text = "Gospodarze", color = Color.LightGray,
                            fontSize = 10.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = btnColor,
                            unfocusedBorderColor = Gray,
                            disabledBorderColor = btnColor,
                            disabledTextColor = Color.White
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                    )
                    Text(
                        text = ":",
                        color = Color.White,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    )

                    OutlinedTextField(
                        value = guestScore,
                        onValueChange = onGuestScoreChange,
                        label = { Text(text = "Goście", color = Color.LightGray,
                            fontSize = 10.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = btnColor,
                            unfocusedBorderColor = Gray,
                            disabledBorderColor = btnColor,
                            disabledTextColor = Color.White
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                    )
                }

                OutlinedTextField(
                    value = guest.name,
                    onValueChange = {},
                    label = { Text(text = "Gość*", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { guestExpanded = !guestExpanded },
                    enabled = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = btnColor,
                        unfocusedBorderColor = Gray,
                        disabledBorderColor = btnColor,
                        disabledTextColor = Color.White
                    )
                )
                DropdownMenu(
                    expanded = guestExpanded,
                    onDismissRequest = { guestExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    teams.forEach { team ->
                        DropdownMenuItem(onClick = {
                            onGuestChange(team)
                            guestExpanded = false
                        },
                            text = { Text(text = team.name) })
                    }
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = onLocationChange,
                    label = { Text(text = "Lokalizacja", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Gray,
                        focusedBorderColor = LeagueBlue,
                    )
                )
                Row(modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Anuluj")
                    }
                    Button(
                        onClick = {
                            onSubmit()
                        },
                        interactionSource = interactionSource,
                        modifier = Modifier
//                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = btnColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Dodaj")
                    }
                }

            }
        }
    )


}