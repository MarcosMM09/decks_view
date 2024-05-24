package com.example.yugiohdeck.viewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yugiohdeck.viewModel.client.KtorClient
import com.example.yugiohdeck.model.ResponseService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardSetViewModel: ViewModel() {

    private var ktorClient = KtorClient()

    private val _cardSets = mutableStateOf<List<ResponseService>>(emptyList())
    val cardSets: State<List<ResponseService>> = _cardSets

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    suspend fun fetchData(): List<ResponseService> {
        return withContext(Dispatchers.IO) {
            try {
                // Lógica para obtener los datos de la API o de la base de datos
                val data = ktorClient.fetchData()
                _cardSets.value = data // Actualizar el estado con los datos obtenidos
                data
            } catch (e: Exception) {
                _error.value = "Error fetching data: ${e.message}"
                emptyList() // Devolver una lista vacía en caso de error
            }
        }
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun getResponseNotNetwork(database: DataDao): List<ResponseService>{
        if(database.obtenerTodos().isNotEmpty()){
            val jsonString = database.obtenerTodos()[0].valor
            val listType = object : TypeToken<List<ResponseService>>() {}.type
            return  Gson().fromJson(jsonString, listType)
        } else {
            val listEmpty: List<ResponseService> = emptyList()
            return listEmpty
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