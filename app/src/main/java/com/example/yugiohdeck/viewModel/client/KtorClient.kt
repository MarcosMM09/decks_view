package com.example.yugiohdeck.viewModel.client

import com.example.yugiohdeck.model.ResponseService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class KtorClient {

    suspend fun fetchData(): List<ResponseService> {
        // Hacer una solicitud HTTP con Ktor
        val client = HttpClient {
            install(Logging) {
                level = LogLevel.BODY
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // Para ignorar campos desconocidos en la respuesta
                })
            }

        }

        val response: HttpResponse = client.get("https://db.ygoprodeck.com/api/v7/cardsets.php") {
            accept(ContentType.Application.Json)
        }

        // Deserializar el cuerpo de la respuesta en una lista de ResponseService
        val responseBody: String = response.bodyAsText()
        val responseList: List<ResponseService> = Json.decodeFromString<List<ResponseService>>(responseBody)

        // Cerrar el cliente después de usarlo
        client.close()
        return responseList
    }

}

