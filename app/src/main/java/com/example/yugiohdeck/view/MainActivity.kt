package com.example.yugiohdeck.view

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yugiohdeck.viewModel.CardSetViewModel
import com.example.yugiohdeck.model.ResponseService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(cardSetViewModel: CardSetViewModel = remember { CardSetViewModel() }) {
    val cardSets by cardSetViewModel.cardSets
    val error by cardSetViewModel.error

    LaunchedEffect(key1 = Unit) {
        cardSetViewModel.fetchData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Yugioh Card Sets",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
            )
        }
    ) {
        Column {
            if (error != null) {
                // Muestra el mensaje de error si hay un error
                // Puedes utilizar un Snackbar, AlertDialog, Text, etc.
                Text(text = error!!)
            } else {
                // Muestra la lista de conjuntos de cartas
                CardSetsList(cardSets)
            }
        }
    }
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

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {
        MainScreen()
    }
}