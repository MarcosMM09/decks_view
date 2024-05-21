package com.example.yugiohdeck.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.room.Room
import com.example.yugiohdeck.model.ResponseService
import com.example.yugiohdeck.viewModel.CardsDatabase
import com.example.yugiohdeck.viewModel.DataDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : ComponentActivity() {

    private val selectedItems = mutableStateListOf<ResponseService>()
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
            withContext(Dispatchers.Main) {
                setContent {
                    MainScreen(cardSets = allCards, context = context,
                        showOptions = false, showButtonFavorites = false, dataCards = dataCards, selectedItems = selectedItems)
                }
            }
        }
    }

    fun getAllResponses(database: DataDao): List<ResponseService>{
        if (database.obtenerTodos().size > 1){
            val jsonString = database.obtenerTodos()[1].valor
            val listType = object : TypeToken<List<ResponseService>>() {}.type
            return  Gson().fromJson(jsonString, listType)
        } else{
            val listEmpty: List<ResponseService> = emptyList()
            return listEmpty
        }
    }
}



