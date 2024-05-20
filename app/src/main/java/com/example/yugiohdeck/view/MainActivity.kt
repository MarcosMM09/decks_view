package com.example.yugiohdeck.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(cardSetViewModel: CardSetViewModel = remember { CardSetViewModel() }) {
    val cardSets by cardSetViewModel.cardSets
    val error by cardSetViewModel.error

    LaunchedEffect(key1 = Unit) {
        cardSetViewModel.fetchData()
    }

    // Estado para controlar la visibilidad del menú flotante
    var expanded by remember { mutableStateOf(false) }

    Scaffold {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
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
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            DropdownMenuItem(text = {
                                Text(text = "favoritos")
                            }, onClick = { /*TODO*/ })
                            // Agrega aquí las opciones del menú
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (error != null) {
                item {
                    // Muestra el mensaje de error si hay un error
                    // Puedes utilizar un Snackbar, AlertDialog, Text, etc.
                    Text(
                        text = error!!,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    )
                }
            } else {
                items(cardSets) { cardSet ->
                    CardSetItem(cardSet = cardSet)
                }
            }
        }
    }
}

@Composable
fun CardSetItem(cardSet: ResponseService) {
    var buttonPressed by remember { mutableStateOf(false) }
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

            // Agregar el botón
            if (!buttonPressed) {
                Button(
                    onClick = {
                        buttonPressed = true // Cambiar el estado del botón
                    },
                ) {
                    Text(text = "Acción")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScreen()
}

// Altura de la AppBar
private val AppBarHeight = 56.dp
