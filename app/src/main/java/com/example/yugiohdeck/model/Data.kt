package com.example.yugiohdeck.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dataService")
data class Data (@PrimaryKey val id: Int,
                 val nombre: String,
                 val valor: String)