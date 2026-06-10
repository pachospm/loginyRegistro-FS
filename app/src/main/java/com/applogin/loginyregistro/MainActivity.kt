package com.applogin.loginyregistro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.applogin.loginyregistro.core.navigation.AppNavGraph
import com.applogin.loginyregistro.ui.theme.LoginyRegistroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginyRegistroTheme {
                AppNavGraph()
            }
        }
    }
}
