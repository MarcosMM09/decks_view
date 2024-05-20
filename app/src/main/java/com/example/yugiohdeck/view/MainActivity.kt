package com.example.yugiohdeck.view

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import com.example.yugiohdeck.model.Data
import com.example.yugiohdeck.model.ResponseService
import com.example.yugiohdeck.view.ui.theme.TopBarUtils
import com.example.yugiohdeck.viewModel.CardSetViewModel
import com.example.yugiohdeck.viewModel.CardsDatabase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            CardsDatabase::class.java, "yogi_oh_db"
        ).build()

        val dataCards = db.mDataUser()
        enableEdgeToEdge()
        // Ejecutar la lógica de inserción de la base de datos en un hilo de fondo
        CoroutineScope(Dispatchers.IO).launch {
            val cardSets = CardSetViewModel().fetchData()
            val gson = Gson()
            val data = Data( 0,"response", gson.toJson(cardSets))
            dataCards.insertOrUpdate(data)
            println("los datos guardados son: ${dataCards.obtenerTodos()}")

            // Después de insertar en la base de datos, mostrar la pantalla principal
            withContext(Dispatchers.Main) {
                setContent {
                    MainScreen(context = this@MainActivity, cardSets = cardSets,
                        showOptions = true, showButtonFavorites = true)
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    context: Context,
    cardSets: List<ResponseService>,
    showOptions: Boolean,
    showButtonFavorites: Boolean
) {
    Scaffold(
        topBar = {
            TopBarUtils.TopAppBarContent(showOptions, context)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
                items(cardSets) { cardSet ->
                    CardSetItem(cardSet = cardSet, showButtonFavorites = showButtonFavorites, onAddToFavoritesClicked = { selectedCardSet ->
                        println("los datos seleccionados son: $selectedCardSet")
                    })
            }
        }
    }
}




@Composable
fun CardSetItem(
    cardSet: ResponseService,
    showButtonFavorites: Boolean,
    onAddToFavoritesClicked: (ResponseService) -> Unit // Función de devolución de llamada
) {
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
            val painter = rememberAsyncImagePainter(model = imageUrl)

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

            if (showButtonFavorites){
                Spacer(modifier = Modifier.height(8.dp))
                // Agregar el botón
                if (!buttonPressed) {
                    Button(
                        onClick = {
                            buttonPressed = true
                            onAddToFavoritesClicked(cardSet) // Llamar a la función de devolución de llamada con los datos del elemento de la lista
                        },
                    ) {
                        Text(text = "Agregar a favoritos")
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Agregado a favoritos")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Agregado a favoritos")
                    }
                }
            }
        }
    }
}



