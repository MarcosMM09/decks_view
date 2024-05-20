package com.example.yugiohdeck.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.yugiohdeck.viewModel.CardSetViewModel
import com.example.yugiohdeck.model.ResponseService
import com.example.yugiohdeck.view.ui.theme.TopBarUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(context = this)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen(cardSetViewModel: CardSetViewModel = remember { CardSetViewModel() }, context: Context) {
    val cardSets by cardSetViewModel.cardSets
    val error by cardSetViewModel.error

    LaunchedEffect(key1 = Unit) {
        cardSetViewModel.fetchData()
    }

    Scaffold(
        topBar = {
            TopBarUtils.TopAppBarContent(true, context)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (error != null) {
                item {
                    // Muestra el mensaje de error si hay un error
                    // Puedes utilizar un Snackbar, AlertDialog, Text, etc.
                    Text(
                        text = error!!,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
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
            val imageUrl = cardSet.set_image
            val painter = rememberImagePainter(data = imageUrl)

            if (imageUrl != null){
                Image(
                    painter = painter,
                    contentDescription = "Set Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(8.dp)) // Añadir un espaciado entre la imagen y el texto
            }

            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Nombre de tarjeta: ")
                }
                append(cardSet.set_name)
            })
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Codigo de tarjeta: ")
                }
                append(cardSet.set_code)
            })
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Numero de tarjetas: ")
                }
                append(cardSet.num_of_cards.toChar())
            })

            Spacer(modifier = Modifier.height(8.dp)) // Añadir un espaciado entre los textos y el botón

            // Agregar el botón
            if (!buttonPressed) {
                Button(
                    onClick = {
                        buttonPressed = true // Cambiar el estado del botón
                    },
                ) {
                    Text(text = "Agregar a favoritos")
                }
            }
        }
    }
}

