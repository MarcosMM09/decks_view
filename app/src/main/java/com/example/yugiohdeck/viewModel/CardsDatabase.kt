package com.example.yugiohdeck.viewModel

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yugiohdeck.model.Data

@Database(entities = [Data::class], version = 5)
abstract class CardsDatabase: RoomDatabase() {
    abstract fun mDataUser(): DataDao
}