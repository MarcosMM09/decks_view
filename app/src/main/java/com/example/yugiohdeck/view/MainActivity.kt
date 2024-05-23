package com.example.yugiohdeck.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import com.example.yugiohdeck.R
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
    private lateinit var cardSets: List<ResponseService>
    private lateinit var dataCards: DataDao
    private lateinit var gson: Gson
    private val vm: CardSetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gson = Gson()
        val db = Room.databaseBuilder(applicationContext, CardsDatabase::class.java, getString(R.string.db_name)).build()
        dataCards = db.mDataUser()
        enableEdgeToEdge()
        val context = this

        // Ejecutar la lógica de inserción de la base de datos en un hilo de fondo
        CoroutineScope(Dispatchers.IO).launch {
            if (CardSetViewModel().isInternetAvailable(context)){
                cardSets = vm.fetchData()
                val gson = Gson()
                val data = Data( 0,getString(R.string.save_response), gson.toJson(cardSets))
                dataCards.insertOrUpdate(data)
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, getString(R.string.app_mode_offline), Toast.LENGTH_LONG).show()
                }
                cardSets = vm.getResponseNotNetwork(dataCards)
            }

            // Después de insertar en la base de datos, mostrar la pantalla principal
            withContext(Dispatchers.Main) {
                setContent {
                    MainScreen(context = this@MainActivity, cardSets = cardSets, dataCards = dataCards, gson
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
    dataCards: DataDao,
    gson: Gson
) {
    val favoriteList: MutableList<ResponseService> = mutableListOf()
    Scaffold(
        topBar = {
            Components().SimpleTopBar(title = stringResource(id = R.string.title_topbar_main), color = Color.Red)
        },
        floatingActionButton = {
            Components().FloatingButton(icon = Icons.Filled.Star, text = stringResource(id = R.string.title_button_favorites), color = Color.Yellow, onClick = {
                val intent = Intent(context, FavoritesActivity::class.java)
                context.startActivity(intent)
            })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(cardSets){ cardSets ->
                val imageUrl = cardSets.set_image
                val painter = rememberAsyncImagePainter(model = imageUrl)
                if (imageUrl != null)
                    Components().CardViewInfo(painter = painter, cardSet = cardSets,showButton = true, onAddToFavoritesClicked = {
                        favoriteList.add(it)
                        // Insertar el elemento seleccionado en la base de datos en un hilo secundario
                        CoroutineScope(Dispatchers.IO).launch {
                            val data = Data(1, "response", gson.toJson(favoriteList))
                            dataCards.insertOrUpdate(data)
                            println("el tamaño es ${dataCards.obtenerTodos().size}")
                            println("se inserto ${dataCards.obtenerTodos().get(1)}")
                        }
                    })
            }
        }
    }
}






