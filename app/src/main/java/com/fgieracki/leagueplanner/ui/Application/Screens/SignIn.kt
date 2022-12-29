package com.fgieracki.leagueplanner.ui.Application.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fgieracki.leagueplanner.R
import com.fgieracki.leagueplanner.data.local.ContextCatcher
import com.fgieracki.leagueplanner.ui.Application.SignInViewModel
import com.fgieracki.leagueplanner.ui.components.Navigation
import com.fgieracki.leagueplanner.ui.theme.Gray
import com.fgieracki.leagueplanner.ui.theme.LeagueBlue
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ScreenSignIn(viewModel: SignInViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
                 onSignIn: () -> Unit = {}) {

    val passwordVisible = remember { mutableStateOf(false) }
    val username = viewModel.username.collectAsState()
    val password = viewModel.password.collectAsState()



    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.toastChannel.collectLatest {
            Toast.makeText(context, it,
                Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navChannel.collectLatest {
            onSignIn()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Gray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(text = "Sign in",
            color = Color.White,
            fontSize = 30.sp,
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = username.value,
            onValueChange ={ viewModel.onUsernameChange(it) },
            label = { Text(text = "Username",
            color = LeagueBlue) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = LeagueBlue,
            ),
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text(text = "Password",
                color = LeagueBlue) },
            singleLine = true,
            placeholder = { Text("Password") },
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = LeagueBlue,
            ),
            trailingIcon = {
                val image = if (passwordVisible.value)
                    painterResource(id = R.drawable.ic_baseline_visibility_24)
                else painterResource(id = R.drawable.ic_baseline_visibility_off_24)

                // Please provide localized description for accessibility services
                val description = if (passwordVisible.value) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible.value = !passwordVisible.value}){
                    Icon(image, contentDescription = description, tint = LeagueBlue)
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { viewModel.signIn() },
            modifier = Modifier
                .padding(horizontal = 60.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = LeagueBlue)
        ) {
            Text(text = "Sign in",
                color = Color.White,
                fontSize = 20.sp,
            )
        }
    }
}