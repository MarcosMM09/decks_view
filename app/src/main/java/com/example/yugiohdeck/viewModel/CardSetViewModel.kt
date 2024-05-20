package com.example.yugiohdeck.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yugiohdeck.viewModel.client.KtorClient
import com.example.yugiohdeck.model.ResponseService
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
}