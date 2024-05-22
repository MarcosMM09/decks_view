package com.example.yugiohdeck.view

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import com.example.yugiohdeck.R
import com.example.yugiohdeck.model.Data
import com.example.yugiohdeck.model.ResponseService
import com.example.yugiohdeck.view.ui.theme.Components
import com.example.yugiohdeck.view.ui.theme.DarkColor
import com.example.yugiohdeck.view.ui.theme.OrangeBorderColor
import com.example.yugiohdeck.view.ui.theme.Styles
import com.example.yugiohdeck.view.ui.theme.WhiteColor
import com.example.yugiohdeck.viewModel.CardSetViewModel
import com.example.yugiohdeck.viewModel.CardsDatabase
import com.example.yugiohdeck.viewModel.DataDao
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private val selectedItems = mutableStateListOf<ResponseService>()
    private lateinit var cardSets: List<ResponseService>
    private lateinit var dataCards: DataDao// Mover la declaración fuera del onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            CardsDatabase::class.java, getString(R.string.db_name)
        ).build()

        dataCards = db.mDataUser()
        enableEdgeToEdge()
        val context = this

        // Ejecutar la lógica de inserción de la base de datos en un hilo de fondo
        CoroutineScope(Dispatchers.IO).launch {
            if (CardSetViewModel().isInternetAvailable(context)){
                cardSets = CardSetViewModel().fetchData()
                val gson = Gson()
                val data = Data( 0,getString(R.string.save_response), gson.toJson(cardSets))
                dataCards.insertOrUpdate(data)
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, getString(R.string.app_mode_offline), Toast.LENGTH_LONG).show()
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
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            val imageUrl = cardSet.set_image
            val painter = rememberAsyncImagePainter(model = imageUrl)

            if (imageUrl != null){
                Image(
                    painter = painter,
                    contentDescription = "Set Image",
                    modifier = Modifier
                        .height(200.dp)
                        .width(200.dp),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )

                Spacer(modifier = Modifier.height(8.dp)) // Añadir un espaciado entre la imagen y el texto
            }

            Text(buildAnnotatedString {
                withStyle(style = Styles.ParamsStyle.paramsWhite){
                    append(stringResource(id = R.string.card_name))
                }
                withStyle(style = Styles.DataStyle.dataWhite){
                    append(cardSet.set_name)
                }
            })
            Text(buildAnnotatedString {
                withStyle(style = Styles.ParamsStyle.paramsWhite){
                    append(stringResource(id = R.string.card_code))
                }
                withStyle(style = Styles.DataStyle.dataWhite){
                    append(cardSet.set_code)
                }
            })
            Text(buildAnnotatedString {
                withStyle(style = Styles.ParamsStyle.paramsWhite){
                    append(stringResource(id = R.string.card_numbers))
                }
                withStyle(style = Styles.DataStyle.dataWhite){
                    append(cardSet.num_of_cards.toString())
                }
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
                        Text(text = stringResource(id = R.string.select_to_favorites))
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = stringResource(id = R.string.selected_to_favorites))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = stringResource(id = R.string.selected_to_favorites))
                    }
                }
            }
        }
    }
}






