package com.example.yugiohdeck.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.yugiohdeck.view.ui.theme.TopBarUtils
import com.example.yugiohdeck.view.ui.theme.YugiOhDeckTheme

class FavoritesActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YugiOhDeckTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                   TopBarUtils.TopAppBarContent(false, context = this)
                }
            }
        }
    }
}