package com.example.yugiohdeck.view

import android.content.Context
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateListOf
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
import com.example.yugiohdeck.view.ui.theme.Components
import com.example.yugiohdeck.viewModel.CardSetViewModel
import com.example.yugiohdeck.viewModel.CardsDatabase
import com.example.yugiohdeck.viewModel.DataDao
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private val selectedItems = mutableStateListOf<ResponseService>() // Mover la declaración fuera del onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            CardsDatabase::class.java, "yogi_oh_db"
        ).build()

        val dataCards = db.mDataUser()
        enableEdgeToEdge()
        val context = this

        // Ejecutar la lógica de inserción de la base de datos en un hilo de fondo
        CoroutineScope(Dispatchers.IO).launch {
            val cardSets: List<ResponseService>
            if (CardSetViewModel().isInternetAvailable(context)){
                cardSets = CardSetViewModel().fetchData()
                val gson = Gson()
                val data = Data( 0,"response", gson.toJson(cardSets))
                dataCards.insertOrUpdate(data)
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "App en modo sin conexion", Toast.LENGTH_LONG).show()
                }
                cardSets = CardSetViewModel().getResponseNotNetwork(dataCards)
            }

            // Después de insertar en la base de datos, mostrar la pantalla principal
            withContext(Dispatchers.Main) {
                setContent {
                    MainScreen(
                        context = this@MainActivity,
                        cardSets = cardSets,
                        showOptions = true,
                        showButtonFavorites = true,
                        dataCards = dataCards,
                        selectedItems = selectedItems // Pasar la lista como parámetro
                    )
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
    showButtonFavorites: Boolean,
    dataCards: DataDao,
    selectedItems: MutableList<ResponseService> // Añadir la lista como parámetro
) {
    Scaffold(
        topBar = {
            Components.TopAppBarContent(showOptions, context)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(cardSets) { cardSet ->
                CardSetItem(
                    cardSet = cardSet,
                    showButtonFavorites = showButtonFavorites,
                    onAddToFavoritesClicked = { selectedCardSet ->
                        selectedItems.add(selectedCardSet)

                        val gson = Gson()
                        // Insertar el elemento seleccionado en la base de datos en un hilo secundario
                        CoroutineScope(Dispatchers.IO).launch {
                            val data = Data(1, "response", gson.toJson(selectedItems))
                            dataCards.insertOrUpdate(data)
                        }
                    }
                )
            }
        }
    }
}




@Composable
fun CardSetItem(
    cardSet: ResponseService,
    showButtonFavorites: Boolean,
    onAddToFavoritesClicked: (ResponseService) -> Unit
) {
    // Recordar el estado del botón
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
                            onAddToFavoritesClicked(cardSet) // Llamar al callback cuando se hace clic en el botón
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




