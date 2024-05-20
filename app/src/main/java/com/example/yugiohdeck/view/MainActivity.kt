package com.example.yugiohdeck.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.benchmark.perfetto.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yugiohdeck.model.ResponseService
import com.example.yugiohdeck.viewModel.CardSetViewModel
import com.google.gson.Gson

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
    val context = LocalContext.current
    var selectedCardSets by remember { mutableStateOf<List<ResponseService>>(emptyList()) }

    LaunchedEffect(key1 = Unit) {
        cardSetViewModel.fetchData()
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Option 1") }

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
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = "Favoritos")
                            },
                            onClick = {
                                selectedOption = "Option 1"
                                expanded = false
                                val gson = Gson()
                                val favoritesListJson = gson.toJson(selectedCardSets)
                                val intent = Intent(context, FavoritesActivity::class.java)
                                intent.putExtra("favoritesJson", favoritesListJson)
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                if (error != null) {
                    Text(text = error!!)
                } else {
                    CardSetsList(cardSets, onCardSetSelected = { cardSet ->
                        selectedCardSets = selectedCardSets + cardSet
                    })
                }
            }
        }
    )

    // Ejemplo: Mostrar un mensaje con el número de conjuntos seleccionados
    LaunchedEffect(selectedCardSets) {
        Toast.makeText(context, "Total Selected: ${selectedCardSets.size}", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "Total Selected: $selectedCardSets", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun CardSetsList(cardSets: List<ResponseService>, onCardSetSelected: (ResponseService) -> Unit) {
    LazyColumn {
        items(cardSets) { cardSet ->
            CardSetItem(cardSet = cardSet, onCardSetSelected = onCardSetSelected)
        }
    }
}

@Composable
fun CardSetItem(cardSet: ResponseService, onCardSetSelected: (ResponseService) -> Unit) {
    var buttonVisible by remember { mutableStateOf(true) }
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

            if (buttonVisible) {
                Button(
                    onClick = {
                        // Acción a realizar cuando se presiona el botón
                        buttonVisible = false // Ocultar el botón después de hacer clic en él
                        onCardSetSelected(cardSet)
                    }
                ) {
                    Text(text = "Agregar a favoritos")
                }
            }
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