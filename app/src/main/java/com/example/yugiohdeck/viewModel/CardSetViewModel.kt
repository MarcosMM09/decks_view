package com.example.yugiohdeck.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yugiohdeck.viewModel.client.KtorClient
import com.example.yugiohdeck.model.ResponseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardSetViewModel: ViewModel() {

    private var ktorClient = KtorClient()

    val cardSets = mutableStateOf<List<ResponseService>>(emptyList())

    val error = mutableStateOf<String?>(null)

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = ktorClient.fetchData()
                cardSets.value = data
            } catch (e: Exception) {
                error.value = "Error fetching data: ${e.message}"
            }
        }
    }
}