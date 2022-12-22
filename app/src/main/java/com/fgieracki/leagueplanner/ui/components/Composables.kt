package com.fgieracki.leagueplanner.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerColors
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


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
    LazyColumn(
        modifier = modifier
    ) {
        items(matches.size){ matchId ->
            MatchItem(mapMatchAndTeamToMatchDisplay(matches[matchId], teams));
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
    date: LocalDate, onDateChange: (LocalDate) -> Unit = {},
    time: LocalTime, onTimeChange: (LocalTime) -> Unit = {},
    location: String, onLocationChange: (String) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    val btnColor = if (isPressed) Color.Green else LeagueBlue
    var hostExpanded by remember { mutableStateOf(false) }
    var guestExpanded by remember { mutableStateOf(false) }
    val dateExpandedState = rememberMaterialDialogState()
    val timeExpandedState = rememberMaterialDialogState()


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
                        singleLine = true,
                        label = {
                            Text(
                                text = "Gospodarze", color = Color.LightGray,
                                fontSize = 10.sp
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = btnColor,
                            unfocusedBorderColor = LeagueBlue,
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
                        singleLine = true,
                        label = {
                            Text(
                                text = "Goście", color = Color.LightGray,
                                fontSize = 10.sp
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = btnColor,
                            unfocusedBorderColor = LeagueBlue,
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
                    singleLine = true,
                    label = { Text(text = "Lokalizacja", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = LeagueBlue,
                        focusedBorderColor = LeagueBlue,
                    )
                )

                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedTextField(
                        value = "${date.dayOfMonth}/${date.monthValue}/${date.year}",
                        onValueChange = {},
                        enabled = false,
//                        readOnly = true,
                        label = { Text(text = "Data", color = Color.LightGray) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = LeagueBlue,
                            focusedBorderColor = LeagueBlue,
                            disabledTextColor = Color.White,
                            disabledBorderColor = LeagueBlue
                        ),
                        modifier = Modifier
                            .weight(2f)
                            .clickable {
                                dateExpandedState.show()
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                        onValueChange = {},
                        enabled = false,
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center
                        ),
                        label = { Text(text = "Czas", color = Color.LightGray) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = LeagueBlue,
                            focusedBorderColor = LeagueBlue,
                            disabledTextColor = Color.White,
                            disabledBorderColor = LeagueBlue
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                timeExpandedState.show()
                            }
                    )
                }

                MaterialDialog( //date picker dialog
                    dialogState = dateExpandedState,
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, LeagueBlue),
                    backgroundColor = Color.LightGray,
                    buttons = {
                        positiveButton(text = "Ok"){
                            onDateChange
                        }
                        negativeButton(text = "Anuluj")

                    }
                ) {
                    datepicker(
                        initialDate = date,
                        onDateChange = onDateChange,
                        title = "Wybierz datę",
                        locale = Locale("pl", "PL"),
                        waitForPositiveButton = false,
                        colors = DatePickerDefaults.colors(
                            headerBackgroundColor = LeagueBlue,
                            headerTextColor = Color.White,
                            dateActiveBackgroundColor = LeagueBlue,
                        )
                    )
                }

                MaterialDialog( //time picker dialog
                    dialogState = timeExpandedState,
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, LeagueBlue),
                    backgroundColor = Color.LightGray,
                    buttons = {
                        positiveButton(text = "Ok"){
                            onDateChange
                        }
                        negativeButton(text = "Anuluj")

                    }
                ) {
                    timepicker(
                        initialTime = time,
                        onTimeChange = onTimeChange,
                        title = "Wybierz czas",
                        waitForPositiveButton = false,
                        is24HourClock = true,
                        colors = TimePickerDefaults.colors(
                            selectorColor = LeagueBlue,
                            selectorTextColor = Color.White,
                            headerTextColor = Color.White,
                            activeBackgroundColor = LeagueBlue,
                        )
                    )
                }


                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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