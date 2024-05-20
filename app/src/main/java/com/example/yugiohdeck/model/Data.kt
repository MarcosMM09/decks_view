package com.example.yugiohdeck.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dataService")
data class Data (@PrimaryKey(autoGenerate = true)
                 val id: Int =0,
                 val nombre: String,
                 val valor: String)