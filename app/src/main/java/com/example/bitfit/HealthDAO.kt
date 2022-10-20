package com.example.bitfit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthDAO {

    @Query("SELECT * FROM HealthData")
    fun getAll(): Flow<List<HeathEntity>>

    @Insert
    fun insertAll(food: HeathEntity)


    @Query("DELETE FROM HealthData")
    fun deleteAll()


    @Delete
    fun deleteItem(food: HeathEntity)




}