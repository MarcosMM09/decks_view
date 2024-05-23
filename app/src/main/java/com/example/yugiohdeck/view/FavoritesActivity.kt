package com.example.yugiohdeck.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : ComponentActivity() {

    private lateinit var dataCards: DataDao
    private val vm: CardSetViewModel by viewModels()
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Room.databaseBuilder(
            applicationContext,
            CardsDatabase::class.java, getString(R.string.db_name)
        ).build()

        dataCards = db.mDataUser()
        CoroutineScope(Dispatchers.IO).launch {
            val allCards = vm.getAllResponses(dataCards)
            withContext(Dispatchers.Main) {
                setContent {
                   FavoritesScreen(cardSets = allCards)
                }
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun FavoritesScreen(
        cardSets: List<ResponseService>,
    ){
        Scaffold(
            topBar = {
                Components().SimpleTopBar(title = stringResource(id = R.string.title_topbar_favorites))
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
                        Components().CardViewInfo(painter = painter, cardSet = cardSets,showButton = false, onAddToFavoritesClicked = {
                        })
                }
            }
        }
    }
}



