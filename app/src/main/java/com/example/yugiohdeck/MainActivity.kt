package com.example.yugiohdeck

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yugiohdeck.client.KtorClient
import com.example.yugiohdeck.model.ResponseService
import com.example.yugiohdeck.ui.theme.YugiOhDeckTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadData() {
        var data: List<ResponseService>
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                // Realiza operaciones intensivas aquí
                val myClient = KtorClient()
                data = myClient.fetchData()
            }

            data.forEach {
                println("estos son los datos: $it")
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    var cardSets by remember { mutableStateOf<List<ResponseService>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = Unit) {
        try {
            val myClient = KtorClient()
            val data = withContext(Dispatchers.IO) {
                myClient.fetchData()
            }
            cardSets = data
        } catch (e: Exception) {
            error = "Error fetching data: ${e.message}"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Card Sets") }
            )
        },
        content = {
            if (error != null) {
                Text(text = error!!, color = Color.Red, modifier = Modifier.padding(16.dp))
            } else {
                CardSetsList(cardSets)
            }
        }
    )
}

@Composable
fun CardSetsList(cardSets: List<ResponseService>) {
    LazyColumn {
        items(cardSets) { cardSet ->
            CardSetItem(cardSet = cardSet)
        }
    }
}

@Composable
fun CardSetItem(cardSet: ResponseService) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Set Name: ${cardSet.set_name}")
            Text(text = "Set Code: ${cardSet.set_code}")
            Text(text = "Number of Cards: ${cardSet.num_of_cards}")
            // Agrega otros campos aquí según sea necesario
        }
    }
}