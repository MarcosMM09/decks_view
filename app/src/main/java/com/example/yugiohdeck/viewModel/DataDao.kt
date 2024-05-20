package com.example.yugiohdeck.viewModel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.yugiohdeck.model.Data

@Dao
interface DataDao {

    @Query("SELECT * FROM dataService")
    fun obtenerTodos(): List<Data>

    @Insert
    fun insertar(mData: Data)

    @Delete
    fun eliminar(mData: Data)
}