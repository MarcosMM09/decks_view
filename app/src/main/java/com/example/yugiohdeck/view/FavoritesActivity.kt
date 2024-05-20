package com.example.yugiohdeck.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.yugiohdeck.model.ResponseService
import com.example.yugiohdeck.view.ui.theme.TopBarUtils
import com.example.yugiohdeck.view.ui.theme.YugiOhDeckTheme
import com.example.yugiohdeck.viewModel.CardsDatabase
import com.example.yugiohdeck.viewModel.DataDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Room.databaseBuilder(
            applicationContext,
            CardsDatabase::class.java, "yogi_oh_db"
        ).build()

        val dataCards = db.mDataUser()
        val context = this
        CoroutineScope(Dispatchers.IO).launch {
            val allCards = getAllResponses(dataCards)
            println("los datos de la db: $allCards")

            withContext(Dispatchers.Main) {
                setContent {
                    MainScreen(cardSets = allCards, context = context,
                        showOptions = false, showButtonFavorites = false)
                }
            }
        }
    }

    fun getAllResponses(database: DataDao): List<ResponseService>{
        val jsonString = database.obtenerTodos()[0].valor
        val listType = object : TypeToken<List<ResponseService>>() {}.type
        return  Gson().fromJson(jsonString, listType)
    }
}



