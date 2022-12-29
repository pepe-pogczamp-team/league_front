package com.fgieracki.leagueplanner.ui.Application.Screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.fgieracki.leagueplanner.data.local.ContextCatcher
import com.fgieracki.leagueplanner.ui.components.*
import com.fgieracki.leagueplanner.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextCatcher.setContext(applicationContext)
        setContent {
            LeaguePlannerTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LightGray,
                ) {
                    Navigation()
                } // Surface
            } // LeaguePlannerTheme
        } //setContent
    } // onCreate

    override fun onDestroy() {
        ContextCatcher.destroyContext()
        super.onDestroy()
    }

} // MainActivity
