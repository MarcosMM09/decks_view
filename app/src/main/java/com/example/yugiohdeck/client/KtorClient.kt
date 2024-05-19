package com.example.yugiohdeck.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class KtorClient {

    suspend fun fetchData(): String {
        return withContext(Dispatchers.IO) {
            // Hacer una solicitud HTTP con Ktor
            val client = HttpClient(CIO){
                install(Logging) {
                    level = LogLevel.BODY
                }
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = false
                    })
                }
            }
            val response = client.get("https://db.ygoprodeck.com/api/v7/cardsets.php")
            val responseBody = response.bodyAsText()
            responseBody
        }
    }
}

@Serializable
data class MyData(val id: Int, val name: String)