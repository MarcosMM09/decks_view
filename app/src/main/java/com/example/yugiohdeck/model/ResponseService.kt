package com.example.yugiohdeck.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseService(
    val set_name: String,
    val set_code: String,
    val num_of_cards: Int,
    val tcg_date: String,
    val set_image: String? = null
)